package com.ehmeed.jvm

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.net.Command
import com.ehmeed.jvm.bot.Bot.Companion.Bot
import io.ktor.application.Application
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

@OptIn(ExperimentalCoroutinesApi::class)
private val gameUpdates = ConflatedBroadcastChannel<List<Game>>()
private val commands = Channel<Command>(Channel.UNLIMITED)

private const val FPS: Long = 50
private const val tickInterval: Long = 1000 / FPS

private val games: MutableList<Game> = mutableListOf()

@OptIn(ExperimentalCoroutinesApi::class)
fun Application.snakeModule() {
    games.add(Game("todo", 20, 1400, 700))
    repeat(10) { Bot(it.toString(), "todo", gameUpdates.openSubscription(), commands) }
    launch(Dispatchers.Default) {
        while (isActive) {
            games.forEach(Game::tick)
            gameUpdates.send(games)
            delay(tickInterval)
            commands.pollAll().forEach { handle(it) }
            games.removeAll { !it.isActive() }
        }
    }
    routing(commands, gameUpdates)
}

private fun handle(command: Command) {
    when (command) {
        is Command.Register -> register(command)
        is Command.Turn -> turn(command)
        is Command.CreateGame -> createGame(command)
    }
}

private fun createGame(command: Command) {
    if (games.none { it.id == command.gameId }) {
        games.add(Game(command.gameId, 20, 800, 600))
    }
}

private fun register(command: Command.Register) {
    games.find { it.id == command.gameId }?.addPlayer(command.playerId)
}


private fun turn(command: Command.Turn) {
    games.find { it.id == command.gameId }?.snakes?.find { it.id == command.playerId }?.changeDirection(command.direction)
}
