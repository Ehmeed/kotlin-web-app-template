package com.ehmeed.common.snake.domain

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class RegularSnake(
    override val id: String,
    override var position: MutableList<Position>,
    override var direction: Direction = Direction.values().random(),
    override var speed: Int = 11
) : Snake {

    private var tail: Position = position.last()

    override val score: Int
        get() = this.position.size

    private var ticksStanding = 0

    init {
        require(position.isNotEmpty()) { "Cannot create snake with no position" }
    }

    override fun head(): Position = position.first()

    override fun occupies(position: Position) = position in this.position

    override fun onApplePossiblyEaten(apple: Apple?) {
        when (apple) {
            is Apple.RedTomato -> position.add(tail)
            is Apple.RedMushroom -> {
                val random = Random.nextDouble()
                when (random) {
                    in 0.0..0.20 -> repeat(5) { position.add(tail) }
                    in 0.20..0.40 -> position = position.take((position.size - 5).coerceAtLeast(1)).toMutableList()
                    in 0.40..0.60 -> speed += 5
                    in 0.60..0.80 -> speed = (speed - 5).coerceAtLeast(1)
                    in 0.80..0.95 -> ticksStanding -= 500
                    in 0.95..1.00 -> repeat(10) { position.add(tail) }
                }
            }
        }
    }

    override fun changeDirection(direction: Direction) {
        this.direction = direction
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun tick(blockSize: Int) {
        ticksStanding += 1
        if (ticksStanding < speed) return
        ticksStanding = 0
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
