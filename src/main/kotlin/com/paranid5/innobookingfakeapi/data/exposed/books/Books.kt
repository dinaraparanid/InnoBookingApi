package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.exposed.rooms.Rooms
import com.paranid5.innobookingfakeapi.data.exposed.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

data object Books : IntIdTable() {
    val title = varchar("title", 100)
    val start = datetime("start")
    val end = datetime("end")

    val room = reference(
        name = "room",
        foreign = Rooms,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val owner = reference(
        name = "owner",
        foreign = Users,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}