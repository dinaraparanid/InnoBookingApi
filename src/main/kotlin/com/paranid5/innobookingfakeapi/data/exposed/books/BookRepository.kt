package com.paranid5.innobookingfakeapi.data.exposed.books

import com.paranid5.innobookingfakeapi.data.exposed.AsyncRepository
import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomDao
import com.paranid5.innobookingfakeapi.data.exposed.users.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

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
                    .find { (Books.start lessEq start) and (Books.end lessEq end) }
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
                            (Books.start lessEq start) and
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
                            (Books.start lessEq start) and
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

    suspend inline fun getByOwnerInDurationAsync(
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
                            (Books.start lessEq start) and
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
        launch(Dispatchers.IO) {
            BookDao.new {
                this.title = title
                this.start = start
                this.end = end
                this.room = room
                this.owner = owner
            }
        }
    }

    suspend inline fun updateAsync(book: BookDao, crossinline action: BookDao.() -> Unit) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { action(book) }
        }
    }
}