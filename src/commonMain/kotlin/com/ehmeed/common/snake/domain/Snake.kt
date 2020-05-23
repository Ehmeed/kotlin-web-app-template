package com.ehmeed.common.snake.domain


interface Snake {
    val position: MutableList<Position>
    val id: String
    val direction: Direction
    val score: Int
    val speed: Int
    fun head(): Position
    fun tick(blockSize: Int)
    fun occupies(position: Position): Boolean
    fun onApplePossiblyEaten(apple: Apple?)
    fun changeDirection(direction: Direction)
}
