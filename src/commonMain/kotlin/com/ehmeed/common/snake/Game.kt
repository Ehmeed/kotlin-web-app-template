package com.ehmeed.common.snake

import com.ehmeed.common.snake.domain.Apple
import com.ehmeed.common.snake.domain.Position
import com.ehmeed.common.snake.domain.RegularSnake
import com.ehmeed.common.snake.domain.Snake
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.random.nextInt

@Serializable
class Game(
    val blockSize: Int = 20,
    val width: Int,
    val height: Int,
    val snakes: MutableList<Snake> = mutableListOf(),
    val apples: MutableList<Apple> = mutableListOf()
) {
    fun step() {
        val eatenApplesPositions =
            snakes.onEach { it.step(blockSize) }
                .filter { apples.any { apple -> apple.position == it.head() } }
                .onEach { it.growTail() }
                .map { it.head() }
        apples.removeAll { it.position in eatenApplesPositions }
        if (apples.isEmpty()) addApple()
//        repeat(eatenApplesPositions.size) { apples.add(Apple(randomEmptyPosition())) }
        snakes.removeAll { it.head().isInCollision() }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun generateRandomSnake(id: String): Snake = RegularSnake(
        id = id,
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

    private fun randomEmptyPosition(): Position  {
        var position: Position? = null
        while (position == null || position.isOccupied()) {
            val x = (1 until width).shuffled().first { it % blockSize == 0 }
            val y = (1 until height).shuffled().first { it % blockSize == 0 }
            position = Position(x, y)
        }
        return position!!
    }

    private fun Position.isOccupied(): Boolean = snakes.any { it.occupies(this) } || apples.any { it.occupies(this) }

    private fun Position.isInCollision(): Boolean  {
        return x < 0 || x > width || y < 0 || y > height || snakes.flatMap { it.position }.count { it == this } > 1
    }

    override fun toString(): String {
        return """
            snakes: ${this.snakes}
            apples: ${this.apples}
        """.trimIndent()
    }
}
