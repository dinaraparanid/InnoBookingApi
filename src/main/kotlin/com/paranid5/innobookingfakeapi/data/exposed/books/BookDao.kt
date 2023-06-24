package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomDao
import com.paranid5.innobookingfakeapi.data.exposed.users.UserDao
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BookDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookDao>(Books)

    var title by Books.title
    var start by Books.start
    var end by Books.end
    var room by RoomDao referencedOn Books.room
    var owner by UserDao referencedOn Books.owner
}