package com.ehmeed.common.snake.domain

data class Apple(val position: Position) {
    fun occupies(position: Position) = this.position == position
}
