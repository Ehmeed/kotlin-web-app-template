package com.ehmeed.common.snake.domain

import kotlinx.serialization.Serializable

@Serializable
data class Apple(val position: Position) {
    fun occupies(position: Position) = this.position == position
}
