package com.paranid5.innobookingfakeapi.data.exposed.rooms

import com.paranid5.innobookingfakeapi.data.exposed.AsyncRepository
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object RoomRepository : AsyncRepository<String, RoomDao> {
    override val table by lazy { Rooms }
    override val dao by lazy { RoomDao }

    override suspend fun createTableAsync() = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction {
                if (!Rooms.exists()) {
                    SchemaUtils.create(Rooms)
                    addAvailableRoomsAsync().join()
                }
            }
        }
    }

    suspend inline fun getByNameAsync(name: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                RoomDao.find { Rooms.name eq name }.singleOrNull()
            }
        }
    }

    suspend inline fun addAsync(id: String, name: String, type: String, capacity: Int) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                RoomDao.new(id) {
                    this.name = name
                    this.type = type
                    this.capacity = capacity
                }
            }
        }
    }

    private suspend inline fun addNoTransactionAsync(id: String, name: String, type: String, capacity: Int) = coroutineScope {
        async(Dispatchers.IO) {
            RoomDao.new(id) {
                this.name = name
                this.type = type
                this.capacity = capacity
            }
        }
    }

    suspend inline fun updateAsync(room: RoomDao, crossinline action: RoomDao.() -> Unit) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { action(room) }
        }
    }

    private suspend inline fun addAvailableRoomsAsync() = coroutineScope {
        launch(Dispatchers.IO) {
            listOf(
                addNoTransactionAsync(
                    id = "301",
                    name = "Lecture Room #301",
                    type = "LECTURE_ROOM",
                    capacity = 24
                ),

                addNoTransactionAsync(
                    id = "303",
                    name = "Lecture Room #303",
                    type = "LECTURE_ROOM",
                    capacity = 30
                ),

                addNoTransactionAsync(
                    id = "304",
                    name = "Lecture Room #304",
                    type = "LECTURE_ROOM",
                    capacity = 25
                ),

                addNoTransactionAsync(
                    id = "305",
                    name = "Lecture Room #305",
                    type = "LECTURE_ROOM",
                    capacity = 25
                ),

                addNoTransactionAsync(
                    id = "312",
                    name = "Lecture Room #312",
                    type = "LECTURE_ROOM",
                    capacity = 30
                ),

                addNoTransactionAsync(
                    id = "313",
                    name = "Lecture Room #313",
                    type = "LECTURE_ROOM",
                    capacity = 60
                ),

                addNoTransactionAsync(
                    id = "314",
                    name = "Lecture Room #314",
                    type = "LECTURE_ROOM",
                    capacity = 34
                ),

                addNoTransactionAsync(
                    id = "318",
                    name = "Lecture Room #318",
                    type = "LECTURE_ROOM",
                    capacity = 30
                ),

                addNoTransactionAsync(
                    id = "320",
                    name = "Lecture Room #320",
                    type = "LECTURE_ROOM",
                    capacity = 28
                ),

                addNoTransactionAsync(
                    id = "3.1",
                    name = "Meeting Room #3.1",
                    type = "MEETING_ROOM",
                    capacity = 6
                ),

                addNoTransactionAsync(
                    id = "3.2",
                    name = "Meeting Room #3.2",
                    type = "MEETING_ROOM",
                    capacity = 6
                ),

                addNoTransactionAsync(
                    id = "3.3",
                    name = "Meeting Room #3.3",
                    type = "MEETING_ROOM",
                    capacity = 6
                ),

                addNoTransactionAsync(
                    id = "3.4",
                    name = "Meeting Room #3.4",
                    type = "MEETING_ROOM",
                    capacity = 6
                ),

                addNoTransactionAsync(
                    id = "3.5",
                    name = "Meeting Room #3.5",
                    type = "MEETING_ROOM",
                    capacity = 6
                )
            ).joinAll()
        }
    }
}