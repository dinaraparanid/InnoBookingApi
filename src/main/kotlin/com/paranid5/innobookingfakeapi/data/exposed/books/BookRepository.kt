package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.exposed.AsyncRepository
import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomDao
import com.paranid5.innobookingfakeapi.data.exposed.rooms.Rooms
import com.paranid5.innobookingfakeapi.data.exposed.users.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object BookRepository : AsyncRepository<Int, BookDao> {
    override val table by lazy { Books }
    override val dao by lazy { BookDao }

    suspend inline fun getByTitleAsync(title: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find { Books.title eq title }.toList()
            }
        }
    }

    suspend inline fun getLatestByTitleAsync(title: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao
                    .find { Books.title eq title }
                    .maxByOrNull { it.start }
            }
        }
    }

    suspend inline fun getAfterStartAsync(start: LocalDateTime) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find { Books.start greaterEq start }.toList()
            }
        }
    }

    suspend inline fun getBeforeEndAsync(end: LocalDateTime) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find { Books.end lessEq end }.toList()
            }
        }
    }

    suspend inline fun getInDurationAsync(start: LocalDateTime, end: LocalDateTime) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao
                    .find { (Books.start greaterEq start) and (Books.end lessEq end) }
                    .toList()
            }
        }
    }

    suspend inline fun getByRoomAsync(roomId: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find { Books.room eq roomId }.toList()
            }
        }
    }

    suspend inline fun getByRoomInDurationAsync(roomId: String, start: LocalDateTime, end: LocalDateTime) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find {
                    (Books.room eq roomId) and
                            (Books.start greaterEq start) and
                            (Books.end lessEq end)
                }.toList()
            }
        }
    }

    suspend inline fun getByOwnerAsync(ownerId: Int) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find { Books.owner eq ownerId }.toList()
            }
        }
    }

    suspend inline fun getByOwnerInDurationAsync(ownerId: Int, start: LocalDateTime, end: LocalDateTime) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find {
                    (Books.owner eq ownerId) and
                            (Books.start greaterEq start) and
                            (Books.end lessEq end)
                }.toList()
            }
        }
    }

    suspend inline fun getByRoomAndOwnerAsync(roomId: String, ownerId: Int) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao
                    .find { (Books.room eq roomId) and (Books.owner eq ownerId) }
                    .toList()
            }
        }
    }

    suspend inline fun getByRoomAndOwnerInDurationAsync(
        roomId: String,
        ownerId: Int,
        start: LocalDateTime,
        end: LocalDateTime
    ) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find {
                    (Books.room eq roomId) and
                            (Books.owner eq ownerId) and
                            (Books.start greaterEq start) and
                            (Books.end lessEq end)
                }.toList()
            }
        }
    }

    suspend inline fun getTotalDayBookTimeForOwner(ownerId: Int, date: LocalDate) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                getByOwnerInDurationAsync(
                    ownerId = ownerId,
                    start = LocalDateTime.of(
                        date.year,
                        date.month,
                        date.dayOfMonth,
                        0,
                        0
                    ),
                    end = LocalDateTime.of(
                        date.year,
                        date.month,
                        date.dayOfMonth,
                        23,
                        59
                    )
                ).await().sumOf { ChronoUnit.MINUTES.between(it.start, it.end) }
            }
        }
    }

    suspend inline fun getFreeRoomsAsync(
        start: LocalDateTime,
        end: LocalDateTime
    ) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                val bookedRooms = getInDurationAsync(start, end).await().map { it.room.id }
                RoomDao.find { Rooms.id notInList bookedRooms }.toList()
            }
        }
    }

    suspend inline fun getByRoomsOrOwnersOrInDurationAsync(
        roomsId: List<String>,
        ownersId: List<Int>,
        start: LocalDateTime,
        end: LocalDateTime
    ) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.find {
                    (Books.room inList roomsId) and
                            (Books.owner inList ownersId) and
                            (Books.start greaterEq start) and
                            (Books.end lessEq end)
                }.toList()
            }
        }
    }

    suspend inline fun addAsync(
        title: String,
        start: LocalDateTime,
        end: LocalDateTime,
        room: RoomDao,
        owner: UserDao
    ) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                BookDao.new {
                    this.title = title
                    this.start = start
                    this.end = end
                    this.room = room
                    this.owner = owner
                }
            }
        }
    }

    suspend inline fun updateAsync(book: BookDao, crossinline action: BookDao.() -> Unit) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { action(book) }
        }
    }

    suspend inline fun deleteAsync(book: BookDao) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { book.delete() }
        }
    }
}