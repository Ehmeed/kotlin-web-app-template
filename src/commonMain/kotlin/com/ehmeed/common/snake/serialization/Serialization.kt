package com.ehmeed.common.snake.serialization

import com.ehmeed.common.snake.domain.RegularSnake
import com.ehmeed.common.snake.domain.Snake
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

val jsonSerializer = Json(JsonConfiguration.Stable, context = SerializersModule {
    polymorphic(Snake::class) {
        RegularSnake::class with RegularSnake.serializer()
    }
})
