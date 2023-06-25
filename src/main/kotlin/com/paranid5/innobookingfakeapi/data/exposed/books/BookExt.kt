package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.BookResponse
import com.paranid5.innobookingfakeapi.data.exposed.rooms.toRoomData
import com.paranid5.innobookingfakeapi.data.extensions.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun BookDao.toBookResponse() = newSuspendedTransaction {
    BookResponse(
        id = this@toBookResponse.id.value.toString(),
        title = title,
        start = start.toKotlinInstant(),
        end = end.toKotlinInstant(),
        room = room.toRoomData(),
        ownerEmail = owner.email
    )
}