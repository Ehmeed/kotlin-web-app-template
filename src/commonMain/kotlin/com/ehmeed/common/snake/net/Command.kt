package com.ehmeed.common.snake.net

import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.serialization.jsonSerializer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.stringify

@Serializable
sealed class Command {
    abstract val playerId: String
    abstract val gameId: String

    @Serializable
    data class Register(override val playerId: String, override val gameId: String) : Command()

    @Serializable
    data class CreateGame(override val playerId: String, override val gameId: String) : Command()

    @Serializable
    data class Turn(override val playerId: String, override val gameId: String, val direction: Direction) : Command()

}

