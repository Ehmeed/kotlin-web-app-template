package com.ehmeed.common.snake.domain

class RegularSnake(override val position: MutableList<Position>, private var direction: Direction = Direction.values().random()) : Snake {

    private var tail: Position = position.last()

    init {
        require(position.isNotEmpty()) { "Cannot create snake with no position" }
    }

    override fun head(): Position = position.first()

    override fun occupies(position: Position) = position !in this.position

    override fun growTail() {
        position.add(tail)
    }

    override fun changeDirection(direction: Direction) {
        this.direction = direction
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun step(): Unit {
        val target = with(head()) {
            when (direction) {
                Direction.UP -> copy(y = y - 1)
                Direction.DOWN -> copy(y = y + 1)
                Direction.LEFT -> copy(x = x - 1)
                Direction.RIGHT -> copy(x = x + 1)
            }
        }
        position.add(0, target)
        tail = position.removeLast()
    }
}
