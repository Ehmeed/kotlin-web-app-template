package com.ehmeed.jvm

import com.ehmeed.common.aaa
import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.delay

fun main() {
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
    routes()
}

private fun Application.routes() {
    install(Routing) {
        get("/") {
            call.respond("root")
        }
        webSocket("/") {
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        outgoing.send(Frame.Text("Ses bul"))
                        delay(2000)
                        outgoing.send(Frame.Text("Jo to teda ses"))
                    }
                }
            }
        }
    }
}
