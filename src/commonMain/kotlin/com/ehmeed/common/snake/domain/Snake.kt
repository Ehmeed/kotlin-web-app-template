package com.ehmeed.common.snake.domain


interface Snake {
    val position: MutableList<Position>
    val id: String
    val direction: Direction
    fun head(): Position
    fun step(blockSize: Int)
    fun occupies(position: Position): Boolean
    fun onAppleEaten()
    fun changeDirection(direction: Direction)
}
