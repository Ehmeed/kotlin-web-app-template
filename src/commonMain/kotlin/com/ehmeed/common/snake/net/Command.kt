package com.ehmeed.common.snake.net

import com.ehmeed.common.snake.domain.Direction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
sealed class Command {
    abstract val snakeId: String

    data class Register(override val snakeId: String) : Command()
    data class Turn(override val snakeId: String, val direction: Direction) : Command()
}

@ImplicitReflectionSerializer
fun main() {
    Json(JsonConfiguration.Stable).toJson(Command.Register("sad"))
}
