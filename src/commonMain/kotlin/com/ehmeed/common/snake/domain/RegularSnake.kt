package com.ehmeed.common.snake.domain

import kotlinx.serialization.Serializable

@Serializable
class RegularSnake(
    override val id: String,
    override val position: MutableList<Position>,
    override var direction: Direction = Direction.values().random()
) : Snake {

    private var tail: Position = position.last()

    override val score: Int
        get() = this.position.size

    init {
        require(position.isNotEmpty()) { "Cannot create snake with no position" }
    }

    override fun head(): Position = position.first()

    override fun occupies(position: Position) = position in this.position

    override fun onAppleEaten() {
        position.add(tail)
    }

    override fun changeDirection(direction: Direction) {
        this.direction = direction
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun step(blockSize: Int): Unit {
        val target = with(head()) {
            when (direction) {
                Direction.UP -> copy(y = y - blockSize)
                Direction.DOWN -> copy(y = y + blockSize)
                Direction.LEFT -> copy(x = x - blockSize)
                Direction.RIGHT -> copy(x = x + blockSize)
            }
        }
        position.add(0, target)
        tail = position.removeLast()
    }
}
