package com.ehmeed.jvm

import com.ehmeed.common.serverPort
import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.net.Command
import com.ehmeed.common.snake.serialization.jsonSerializer
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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.stringify

fun <T : Any> Channel<T>.pollAll(): Sequence<T> {
    return generateSequence { this.poll() }
        .takeWhile { it != null }
}

@OptIn(ExperimentalCoroutinesApi::class)
val gameState = BroadcastChannel<Game>(64)
val commands = Channel<Command>(Channel.UNLIMITED)


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
    routes()
}


@OptIn(ImplicitReflectionSerializer::class, ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
private fun Application.routes() {
    val game = Game(width = 800, height = 600)
    launch(Dispatchers.Default) {
        while (isActive) {
            game.step()
            gameState.send(game)
            delay(1000)
            commands.pollAll().map { cmd -> cmd to game.snakes.find { it.id == cmd.snakeId } }
                .map {
                    println("read command")
                    when (val cmd = it.first) {
                        is Command.Register -> {
                            game.addSnake(cmd.snakeId)
                            game.addApple()
                        }
                        is Command.Turn -> {
                            if (it.second != null) it.second!!.changeDirection(cmd.direction)
                        }
                    }
                }
        }
    }



    install(Routing) {
        get("/") {
            call.respond("root")
        }
        webSocket("/snake") {
            launch() {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            println(text)
                            val command = Command.fromString(text)
                            commands.send(command)
                        }
                    }
                }
            }
            println("Launched sub")
            gameState.consumeEach { update ->
                println("sending update")
                send(Frame.Text(jsonSerializer.stringify(update)))
            }
        }
    }
}
