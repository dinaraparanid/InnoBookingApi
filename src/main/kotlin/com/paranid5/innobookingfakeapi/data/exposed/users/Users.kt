package com.paranid5.innobookingfakeapi.data.exposed.users

import org.jetbrains.exposed.dao.id.IntIdTable

data object Users : IntIdTable() {
    val email = varchar("email", 100).uniqueIndex()
}