package com.paranid5.innobookingfakeapi.data.exposed.rooms

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoomDao(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, RoomDao>(Rooms)

    var name by Rooms.name
    var type by Rooms.type
    var capacity by Rooms.capacity
}