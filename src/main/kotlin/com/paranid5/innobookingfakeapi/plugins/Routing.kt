package com.paranid5.innobookingfakeapi.plugins

import com.paranid5.innobookingfakeapi.data.BookQueryRequest
import com.paranid5.innobookingfakeapi.data.BookRequest
import com.paranid5.innobookingfakeapi.data.BookTimePeriod
import com.paranid5.innobookingfakeapi.data.exposed.books.BookRepository
import com.paranid5.innobookingfakeapi.data.exposed.books.toBookResponse
import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomDao
import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomRepository
import com.paranid5.innobookingfakeapi.data.exposed.rooms.toRoomData
import com.paranid5.innobookingfakeapi.data.exposed.users.UserRepository
import com.paranid5.innobookingfakeapi.data.extensions.toJavaLocalDateTime
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val MAX_MINUTES_PER_BOOK = 60 * 3
private const val MAX_MINUTES_PER_DAY = 60 * 5

fun Application.configureRouting() {
    routing {
        route("/rooms") {
            get { onRoomsReceived() }
            post("/free") { onFreeReceived() }
            post("/{room_id}/book") { onBookReceived() }
        }

        route("/bookings") {
            post("/query") { onBookQueryReceived() }
            delete("/{booking_id}") { onBookCancelReceived() }
        }
    }
}

private suspend inline fun PipelineContext<Unit, ApplicationCall>.onRoomsReceived() =
    call.respond(RoomRepository.getAllAsync().await().map(RoomDao::toRoomData))

private suspend inline fun PipelineContext<Unit, ApplicationCall>.onFreeReceived() {
    val (start, end) = Json.decodeFromString<BookTimePeriod>(call.receiveText())

    call.respond(
        BookRepository.getFreeRoomsAsync(
            start.toJavaLocalDateTime(),
            end.toJavaLocalDateTime()
        ).await().map(RoomDao::toRoomData)
    )
}

private inline val LocalDateTime.inAvailableForBookingHourRange
    get() = (hour >= 19 || hour < 8)

private suspend inline fun PipelineContext<Unit, ApplicationCall>.onBookReceived() {
    val (title, startKt, endKt, ownerEmail) = Json.decodeFromString<BookRequest>(call.receiveText())

    val start = startKt.toJavaLocalDateTime()
    val end = endKt.toJavaLocalDateTime()
    val currentTime = Clock.System.now().toJavaLocalDateTime()

    if (start <= currentTime || end <= start) {
        println("Illegal time 1")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    if (!start.inAvailableForBookingHourRange || !end.inAvailableForBookingHourRange) {
        println("Illegal time 2")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    val roomId = call.parameters["room_id"] ?: run {
        println("Room id not found")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    val room = RoomRepository.getByIdAsync(roomId).await()

    if (room == null) {
        println("Room not found")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    if (BookRepository.getByRoomInDurationAsync(roomId, start, end).await().isNotEmpty()) {
        println("Room is booked")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    val owner = UserRepository.getByEmailAsync(ownerEmail).await()
        ?: UserRepository.addAsync(ownerEmail).await()

    val totalTodayMinutes = BookRepository.getTotalDayBookTimeForOwner(
        ownerId = owner.id.value,
        date = start.toLocalDate()
    ).await()

    val bookMinutes = ChronoUnit.MINUTES.between(start, end)

    if (bookMinutes > MAX_MINUTES_PER_BOOK || totalTodayMinutes + bookMinutes > MAX_MINUTES_PER_DAY) {
        println("Time limit exceeded")
        call.response.status(HttpStatusCode.BadRequest)
        return
    }

    call.respond(
        BookRepository
            .addAsync(title, start, end, room, owner)
            .await()
            .toBookResponse()
    )
}

private suspend inline fun PipelineContext<Unit, ApplicationCall>.onBookQueryReceived() {
    val (start, end, ownerEmails, roomsId) = Json.decodeFromString<BookQueryRequest>(call.receiveText()).filter

    println("-------- BOOK REQUEST --------")
    println("Start: $start")
    println("End: $end")
    println("Owner Emails: $ownerEmails")
    println("Room ID: $roomsId")

    val ownersId = UserRepository.getIdByEmailsAsync(ownerEmails).await()

    if (ownersId.size != ownerEmails.size)
        throw RuntimeException("OwnersID != OwnersEmails")

    call.respond(
        BookRepository.getByRoomsOwnersInDurationAsync(
            roomsId = roomsId,
            ownersId = ownersId,
            start = start.toJavaLocalDateTime(),
            end = end.toJavaLocalDateTime()
        ).await().map { it.toBookResponse() }
    )
}

private suspend inline fun PipelineContext<Unit, ApplicationCall>.onBookCancelReceived() {
    val bookId = call.parameters["booking_id"]?.toIntOrNull() ?: run {
        call.response.status(HttpStatusCode.NotFound)
        return
    }

    when (val book = BookRepository.getByIdAsync(bookId).await()) {
        null -> call.response.status(HttpStatusCode.NotFound)

        else -> {
            BookRepository.deleteAsync(book).join()
            call.response.status(HttpStatusCode.OK)
        }
    }
}