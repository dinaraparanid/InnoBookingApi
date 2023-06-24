package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.exposed.rooms.Rooms
import com.paranid5.innobookingfakeapi.data.exposed.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Books : IntIdTable() {
    val title = varchar("title", 100)
    val start = datetime("start")
    val end = datetime("end")
    val room = reference("room", Rooms)
    val owner = reference("owner", Users)
}