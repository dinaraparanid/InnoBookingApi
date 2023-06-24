package com.paranid5.innobookingfakeapi.data.exposed.rooms

import com.paranid5.innobookingfakeapi.data.exposed.AsyncRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object RoomRepository : AsyncRepository<String, RoomDao> {
    override val table by lazy { Rooms }
    override val dao by lazy { RoomDao }

    suspend inline fun getByNameAsync(name: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                RoomDao.find { Rooms.name eq name }.singleOrNull()
            }
        }
    }

    suspend inline fun addAsync(name: String, type: String, capacity: Int) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction {
                RoomDao.new {
                    this.name = name
                    this.type = type
                    this.capacity = capacity
                }
            }
        }
    }

    suspend inline fun updateAsync(room: RoomDao, crossinline action: RoomDao.() -> Unit) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { action(room) }
        }
    }
}