package com.ehmeed.jvm

import com.ehmeed.common.serverPort
import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.net.Command
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.json


@OptIn(ImplicitReflectionSerializer::class)
fun main() = runBlocking<Unit> {
    println("Starting JVM server")

    val server = embeddedServer(
        Netty,
        host = "0.0.0.0",
        port = serverPort,
        watchPaths = listOf("kotlin-web-app-template"),
        module = Application::module
    )
    server.start(wait = true)
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        HttpMethod.DefaultMethods.map { method(it) }
        allowNonSimpleContentTypes = true
        anyHost()
    }
    install(WebSockets)
    snakeModule()
}


