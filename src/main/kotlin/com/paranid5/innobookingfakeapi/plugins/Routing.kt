package com.paranid5.innobookingfakeapi.plugins

import com.paranid5.innobookingfakeapi.data.BookRequest
import com.paranid5.innobookingfakeapi.data.BookResponse
import com.paranid5.innobookingfakeapi.data.Room
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.datetime.Instant

fun Application.configureRouting() {
    routing {
        route("/rooms") {
            get {
                call.respond(
                    listOf(
                        Room(
                            name = "Meeting Room #3.1",
                            id = "3.1",
                            type = "MEETING_ROOM",
                            capacity = 6
                        ),
                        Room(
                            name = "Meeting Room #3.2",
                            id = "3.2",
                            type = "MEETING_ROOM",
                            capacity = 6
                        )
                    )
                )
            }

            post("/free") {
                call.respond(
                    listOf(
                        Room(
                            name = "Meeting Room #3.2",
                            id = "3.2",
                            type = "MEETING_ROOM",
                            capacity = 6
                        )
                    )
                )
            }

            post("/{room_id}/book") {
                val request = call.receive<BookRequest>()
                val params = call.parameters

                call.respond(
                    BookResponse(
                        id = "123",
                        title = request.title,
                        start = request.start,
                        end = request.end,
                        room = Room(
                            name = "Meeting Room #${params["room_id"]}",
                            id = params["room_id"]!!,
                            type = "MEETING_ROOM",
                            capacity = 6
                        ),
                        ownerEmail = request.ownerEmail
                    )
                )
            }
        }

        route("/bookings") {
            post("/query") {
                call.respond(
                    listOf(
                        BookResponse(
                            id = "123",
                            title = "Gay party",
                            start = Instant.parse("2023-06-20T21:31:22.898Z"),
                            end = Instant.parse("2023-06-20T21:31:22.898Z"),
                            room = Room(
                                name = "Meeting Room #32",
                                id = "228",
                                type = "MEETING_ROOM",
                                capacity = 6
                            ),
                            ownerEmail = "g.bebra@innopolis.university"
                        ),
                        BookResponse(
                            id = "453",
                            title = "Kudaskell",
                            start = Instant.parse("2023-06-20T21:31:22.898Z"),
                            end = Instant.parse("2023-06-20T21:31:22.898Z"),
                            room = Room(
                                name = "Room 303",
                                id = "135",
                                type = "ROOM",
                                capacity = 30
                            ),
                            ownerEmail = "s.bebra@innopolis.university"
                        )
                    )
                )
            }

            delete("/{booking_id}") {
                call.response.status(HttpStatusCode.OK)
            }
        }
    }
}
