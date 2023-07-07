package com.paranid5.innobookingfakeapi.data.exposed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface AsyncRepository<Id : Comparable<Id>, E : Entity<Id>> {
    val table: IdTable<Id>
    val dao: EntityClass<Id, E>

    suspend fun createTableAsync() = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { SchemaUtils.create(table) }
        }
    }

    suspend fun getAllAsync() = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction { dao.all().toList() }
        }
    }

    suspend fun getByIdAsync(id: Id) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction { dao.findById(id) }
        }
    }
}