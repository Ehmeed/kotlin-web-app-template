package com.ehmeed.common.snake.domain

import kotlinx.serialization.Serializable

@Serializable
sealed class Apple {
    abstract val position: Position

    fun occupies(position: Position) = this.position == position

    @Serializable
    data class RedTomato(override val position: Position) : Apple()

    @Serializable
    data class RedMushroom(override val position: Position) : Apple()
}
