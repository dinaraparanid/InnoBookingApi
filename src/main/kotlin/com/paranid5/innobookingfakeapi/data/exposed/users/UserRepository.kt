package com.paranid5.innobookingfakeapi.data.exposed.users

import com.paranid5.innobookingfakeapi.data.exposed.AsyncRepository
import com.paranid5.innobookingfakeapi.data.firebase.fetchUsersFromFirestoreAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserRepository : AsyncRepository<Int, UserDao> {
    override val table by lazy { Users }
    override val dao by lazy { UserDao }

    override suspend fun createTableAsync() = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction {
                if (!Users.exists()) {
                    SchemaUtils.create(Users)
                    fetchUsersAsync().join()
                }
            }
        }
    }

    suspend inline fun getByEmailAsync(email: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                UserDao.find { Users.email eq email }.singleOrNull()
            }
        }
    }

    suspend inline fun getByEmailsAsync(emails: List<String>) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                UserDao.find { Users.email inList emails }.toList()
            }
        }
    }

    suspend inline fun getIdByEmailsAsync(emails: List<String>) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                UserDao.find { Users.email inList emails }.map { it.id.value }.toList()
            }
        }
    }

    suspend inline fun addAsync(email: String) = coroutineScope {
        async(Dispatchers.IO) {
            newSuspendedTransaction {
                UserDao.new { this.email = email }
            }
        }
    }

    suspend inline fun updateAsync(user: UserDao, crossinline action: UserDao.() -> Unit) = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction { action(user) }
        }
    }

    private suspend inline fun fetchUsersAsync() = coroutineScope {
        launch(Dispatchers.IO) {
            newSuspendedTransaction {
                fetchUsersFromFirestoreAsync()
                    .await()
                    .forEach { UserDao.new { this.email = it } }
            }
        }
    }
}