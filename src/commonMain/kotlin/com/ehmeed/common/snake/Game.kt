package com.ehmeed.common.snake

import com.ehmeed.common.snake.domain.Apple
import com.ehmeed.common.snake.domain.Position
import com.ehmeed.common.snake.domain.RegularSnake
import com.ehmeed.common.snake.domain.Snake
import kotlin.random.Random
import kotlin.random.nextInt

class Game(
    val width: Int,
    val height: Int,
    val snakes: MutableList<Snake> = mutableListOf(),
    val apples: MutableList<Apple> = mutableListOf()
) {

    init {
        apples.add(Apple(randomEmptyPosition()))
        snakes.add(generateRandomSnake())
    }

    fun step() {
        val eatenApplesPositions =
            snakes.onEach { it.step() }
                .filter { apples.any { apple -> apple.position == it.head() } }
                .onEach { it.growTail() }
                .map { it.head() }
        apples.removeAll { it.position in eatenApplesPositions }
        repeat(eatenApplesPositions.size) { apples.add(Apple(randomEmptyPosition())) }
        snakes.removeAll { it.head().isInCollision() }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun generateRandomSnake(): Snake = RegularSnake(
        position = buildList<Position> {
            val head = randomEmptyPosition()
            add(head)
            val body = Position(head.x + Random.nextInt(-1..1), head.y + Random.nextInt(-1..1))
            if (body == head) {
                add(body.copy(x = body.x + 1))
            } else {
                add(body)
            }
        }.toMutableList()
    )

    private fun randomEmptyPosition(): Position =
        (0..width).shuffled().zip((0..height).shuffled())
            .map { Position(it.first, it.second) }
            .first { !it.isOccupied() }

    private fun Position.isOccupied(): Boolean = snakes.none { it.occupies(this) } && apples.none { it.occupies(this) }

    private fun Position.isInCollision(): Boolean  {
        return x < 0 || x > width || y < 0 || y > height || snakes.flatMap { it.position }.count { it == this } > 1
    }
}
