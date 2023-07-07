package com.paranid5.innobookingfakeapi.data.exposed

import com.paranid5.innobookingfakeapi.data.exposed.books.BookRepository
import com.paranid5.innobookingfakeapi.data.exposed.rooms.RoomRepository
import com.paranid5.innobookingfakeapi.data.exposed.users.UserRepository
import com.paranid5.innobookingfakeapi.data.firebase.configureFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import java.io.File

suspend fun initBookDatabase() = coroutineScope {
    launch(Dispatchers.IO) {
        configureFirebase()

        Database.connect(
            url = "jdbc:sqlite:${
                File("").absolutePath.replace(
                    "\\",
                    "/"
                )
            }/inno_booking.db",
            driver = "org.sqlite.JDBC"
        )
    }.join()

    UserRepository.createTableAsync().join()
    RoomRepository.createTableAsync().join()
    BookRepository.createTableAsync().join()
}

