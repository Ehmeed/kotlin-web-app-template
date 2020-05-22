package com.ehmeed.common.snake

import com.ehmeed.common.snake.domain.Apple
import com.ehmeed.common.snake.domain.Position
import com.ehmeed.common.snake.domain.RegularSnake
import com.ehmeed.common.snake.domain.Snake
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.random.nextInt

@Serializable
data class Game(
    val id: String,
    val blockSize: Int = 20,
    val width: Int,
    val height: Int,
    val snakes: MutableList<Snake> = mutableListOf(),
    val apples: MutableList<Apple> = mutableListOf()
) {
    private var step = 0

    fun tick() {
        val eatenApplesPositions =
            snakes.onEach { it.step(blockSize) }
                .filter { apples.any { apple -> apple.position == it.head() } }
                .onEach { it.onAppleEaten() }
                .map { it.head() }
        apples.removeAll { it.position in eatenApplesPositions }
        if (apples.isEmpty()) addApple()
        snakes.removeAll { it.head().isInCollision() }
        step += 1
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun generateRandomSnake(id: String): Snake = RegularSnake(
        id = id,
        position = buildList<Position> {
            val head = randomEmptyPosition(nearCenter = true)
            add(head)
            val body = Position(head.x + Random.nextInt(-1..1), head.y + Random.nextInt(-1..1))
            if (body == head) {
                add(body.copy(x = body.x + 1))
            } else {
                add(body)
            }
        }.toMutableList()
    )

    fun addPlayer(id: String) {
        println("Adding player id: $id")
        addSnake(id)
        addApple()
    }

    fun addSnake(id: String) {
        this.snakes.add(generateRandomSnake(id))
    }

    fun addApple() {
        this.apples.add(Apple(randomEmptyPosition()))
    }

    private fun randomEmptyPosition(nearCenter: Boolean = false): Position  {
        var position: Position? = null
        val (minX, maxX) = if (nearCenter) {
            width/4 to 3 * (width/4)
        } else {
            1 to width
        }
        val (minY, maxY) = if (nearCenter) {
            height/4 to 3 * (height/4)
        } else {
            1 to height
        }
        while (position == null || position.isOccupied()) {
            val x = (minX until maxX).shuffled().first { it % blockSize == 0 }
            val y = (minY until maxY).shuffled().first { it % blockSize == 0 }
            position = Position(x, y)
        }
        return position!!
    }

    private fun Position.isOccupied(): Boolean = snakes.any { it.occupies(this) } || apples.any { it.occupies(this) }

    private fun Position.isInCollision(): Boolean  {
        return x < 0 || x >= width || y < 0 || y >= height || snakes.flatMap { it.position }.count { it == this } > 1
    }

    fun isActive(): Boolean {
        return true
    }
}
