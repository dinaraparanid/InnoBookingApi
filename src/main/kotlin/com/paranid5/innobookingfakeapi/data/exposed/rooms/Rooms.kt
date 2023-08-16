package com.paranid5.innobookingfakeapi.data.exposed.rooms

import org.jetbrains.exposed.dao.id.IdTable

data object Rooms : IdTable<String>() {
    override val id = varchar("id", 100).entityId()
    val name = varchar("name", 100).uniqueIndex()
    val type = varchar("type", 100)
    val capacity = integer("capacity")
}