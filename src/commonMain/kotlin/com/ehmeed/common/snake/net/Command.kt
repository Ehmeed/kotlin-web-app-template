package com.ehmeed.common.snake.net

import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.serialization.jsonSerializer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.json

@Serializable
sealed class Command {
    abstract val snakeId: String

    @Serializable
    data class Register(override val snakeId: String) : Command()
    @Serializable
    data class Turn(override val snakeId: String, val direction: Direction) : Command()
    @Serializable
    data class RemoveTail(override val snakeId: String): Command()


    // TODO (MH): 2020-05-17 switch out this logic for normal polymorphic serialization (if it works for sealed classes
//    https://github.com/Kotlin/kotlinx.serialization/issues/450
    @OptIn(ImplicitReflectionSerializer::class)
    fun serialize(): String = when (this) {
        is Register -> "register" + "|" + jsonSerializer.toJson(this).toString()
        is Turn -> "turn" + "|" + jsonSerializer.toJson(this).toString()
        is RemoveTail -> "removetail" + "|" + jsonSerializer.toJson(this).toString()
    }

    companion object {
        @OptIn(ImplicitReflectionSerializer::class)
        fun fromString(value: String): Command {
            val (cmd, serialized) = value.split("|")
            val jsonElement = jsonSerializer.parseJson(serialized)
            return when (cmd) {
                "register" -> jsonSerializer.fromJson<Register>(jsonElement)
                "turn" -> jsonSerializer.fromJson<Turn>(jsonElement)
                "removetail" -> jsonSerializer.fromJson<RemoveTail>(jsonElement)
                else -> throw RuntimeException("Unknown command")
            }
        }
    }
}
