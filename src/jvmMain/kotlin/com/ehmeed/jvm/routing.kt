package com.ehmeed.jvm

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.net.Command
import com.ehmeed.common.snake.serialization.jsonSerializer
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.websocket.webSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.stringify


@OptIn(ImplicitReflectionSerializer::class, ExperimentalCoroutinesApi::class)
fun Application.routing(commands: SendChannel<Command>, gameState: BroadcastChannel<List<Game>>) {
    install(Routing) {
        get("/ping") {
            call.respond("pong!")
        }
        webSocket("/snake") {
            var gameId: String? = null
            launch(Dispatchers.Default) {
                for (frame in incoming) {
                    if (frame !is Frame.Text) continue
                    val text = frame.readText()
                    val command = Command.fromString(text)
                    if (command is Command.Register) gameId = command.gameId
                    commands.send(command)
                }
            }

            for (update in gameState.openSubscription()) {
                ensureActive()
                val activeGame = update.firstOrNull { it.id == gameId }
                if (activeGame != null) {
                    val gameUpdate = jsonSerializer.stringify(activeGame)
                    outgoing.send(Frame.Text(gameUpdate))
                }
            }
        }
    }
}
