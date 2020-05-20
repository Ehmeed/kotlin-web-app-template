package com.ehmeed.common.snake.serialization

import com.ehmeed.common.snake.domain.RegularSnake
import com.ehmeed.common.snake.domain.Snake
import com.ehmeed.common.snake.net.Command
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

val jsonSerializer = Json(JsonConfiguration.Stable, context = SerializersModule {
    polymorphic(Snake::class) {
        RegularSnake::class with RegularSnake.serializer()
    }
    polymorphic(Command::class) {
        Command.Register::class with Command.Register.serializer()
        Command.Turn::class with Command.Turn.serializer()
    }
})
