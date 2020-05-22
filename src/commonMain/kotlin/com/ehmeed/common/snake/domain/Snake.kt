package com.ehmeed.common.snake.domain


interface Snake {
    val position: MutableList<Position>
    val id: String
    val direction: Direction
    val score: Int
    fun head(): Position
    fun step(blockSize: Int)
    fun occupies(position: Position): Boolean
    fun onAppleEaten()
    fun changeDirection(direction: Direction)
}
