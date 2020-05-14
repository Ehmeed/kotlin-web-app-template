package com.ehmeed.common.snake.domain

interface Snake {
    val position: MutableList<Position>
    val id: String
    fun head(): Position
    fun step()
    fun occupies(position: Position): Boolean
    fun growTail()
    fun changeDirection(direction: Direction)
}
