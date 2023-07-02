package com.paranid5.innobookingfakeapi

import com.paranid5.innobookingfakeapi.data.exposed.initBookDatabase
import com.paranid5.innobookingfakeapi.plugins.configureHTTP
import com.paranid5.innobookingfakeapi.plugins.configureRouting
import com.paranid5.innobookingfakeapi.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    initBookDatabase()

    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)

    Unit
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
}
