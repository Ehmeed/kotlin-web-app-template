package com.ehmeed.common.snake.domain

interface Snake {
    val position: MutableList<Position>
    fun head(): Position
    fun step()
    fun occupies(position: Position): Boolean
    fun growTail()
    fun changeDirection(direction: Direction)
}
