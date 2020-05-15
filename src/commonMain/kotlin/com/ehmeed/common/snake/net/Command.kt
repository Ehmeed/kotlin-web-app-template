package com.ehmeed.common.snake.net

import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.serialization.jsonSerializer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable

@Serializable
sealed class Command {
    abstract val snakeId: String

    @Serializable
    data class Register(override val snakeId: String) : Command()
    @Serializable
    data class Turn(override val snakeId: String, val direction: Direction) : Command()

    @OptIn(ImplicitReflectionSerializer::class)
    fun serialize(): String = when (this) {
        is Register -> "register" + "|" + jsonSerializer.toJson(this).toString()
        is Turn -> "turn" + "|" + jsonSerializer.toJson(this).toString()
    }

    companion object {
        @OptIn(ImplicitReflectionSerializer::class)
        fun fromString(value: String): Command {
            val (cmd, serialized) = value.split("|")
            val jsonElement = jsonSerializer.parseJson(serialized)
            return when (cmd) {
                "register" -> jsonSerializer.fromJson<Register>(jsonElement)
                "turn" -> jsonSerializer.fromJson<Turn>(jsonElement)
                else -> throw RuntimeException("Unknown command")
            }
        }
    }
}
