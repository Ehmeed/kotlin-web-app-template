package com.ehmeed.jvm.bot

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.net.Command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class Bot(
    private val snakeId: String,
    private val gameId: String,
    private val gameState: ReceiveChannel<List<Game>>,
    private val commands: SendChannel<Command>,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    init {
        launch(Dispatchers.Default) {
            commands.send(Command.Register(snakeId, gameId))
            for (state in gameState) {
                val gameState = state.find { it.id == gameId }
                if (Random.nextDouble() < 0.2)
                    commands.send(Command.Turn(snakeId, gameId, Direction.values().random()))
            }
        }
    }

    companion object {
        fun CoroutineScope.Bot(snakeId: String, gameId: String, gameState: ReceiveChannel<List<Game>>, commands: SendChannel<Command>) =
            Bot(snakeId, gameId, gameState, commands, this.coroutineContext)
    }
}
