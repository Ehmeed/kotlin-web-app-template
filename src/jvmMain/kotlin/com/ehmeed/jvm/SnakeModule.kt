package com.ehmeed.jvm

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Snake
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.stringify
import kotlin.random.Random


fun <T : Any> Channel<T>.pollAll(): Sequence<T> {
    return generateSequence { this.poll() }
        .takeWhile { it != null }
}

@OptIn(ExperimentalStdlibApi::class)
fun Game.handleCommand(cmd: Command, snake: Snake?) {
    when (cmd) {
        is Command.Register -> {
            this.addPlayer(cmd.snakeId)
        }
        is Command.Turn -> {
            snake?.changeDirection(cmd.direction) // // TODO (MH): 2020-05-17 else fail
        }
        is Command.RemoveTail -> {
            snakes.filter { it.id != cmd.snakeId }
                .forEach { if (it.position.size > 1) it.position.removeLast() }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private val gameState = BroadcastChannel<Game>(64)
private val commands = Channel<Command>(Channel.UNLIMITED)

var stepDelay: Long = 300
@OptIn(ImplicitReflectionSerializer::class, ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
fun Application.snakeModule() {
    val game = Game(width = 800, height = 600)
    launch(Dispatchers.Default) {
        while (isActive) {
            game.step()
            with(game) { gameState.send(Game(blockSize, width, height, snakes, apples)) }
            if (Random.nextDouble() < 0.01) repeat(15) { game.addApple() }
            delay(stepDelay)
            commands.pollAll().map { cmd -> cmd to game.snakes.find { it.id == cmd.snakeId } }
                .forEach { game.handleCommand(it.first, it.second) }
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
                            val command = Command.fromString(text)
                            commands.send(command)
                        }
                    }
                }
            }
            gameState.consumeEach { update ->
                val gameUpdate = jsonSerializer.stringify(update)
                send(Frame.Text(gameUpdate))
            }
        }
    }
}
