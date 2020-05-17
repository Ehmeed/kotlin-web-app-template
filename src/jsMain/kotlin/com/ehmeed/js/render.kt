package com.ehmeed.js

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Direction
import org.w3c.dom.CanvasRenderingContext2D


fun CanvasRenderingContext2D.render(game: Game) = withFreshContext(
    { renderBackground(game) },
    { renderBorder(game) },
    { renderSnakes(game) },
    { renderApples(game) }
)

fun CanvasRenderingContext2D.renderGetReadyMessage() {
    fillStyle = "yellow"
    font = "48px serif"
    fillText("Get ready!", 400.toDouble(), 320.toDouble())
}

private fun CanvasRenderingContext2D.renderSnakes(game: Game) {
    val halfSize = game.blockSize / 2.0
    val quarterSize = game.blockSize / 4.0
    for (snake in game.snakes) {
        val head = snake.head()

        fillStyle = if (snake.id == snakeId) "green" else "black"
        snake.position.forEach {
            fillRect(it.x - halfSize, it.y - halfSize, 2 * halfSize, 2 * halfSize)
        }
        val eyeSize = game.blockSize / 8.0
        fillStyle = if (snake.id == snakeId) "black" else "red"
        when (snake.direction) {
            Direction.UP -> {
                fillRect(head.x - quarterSize, head.y + quarterSize, eyeSize, eyeSize)
                fillRect(head.x + quarterSize, head.y + quarterSize, eyeSize, eyeSize)
            }
            Direction.DOWN -> {
                fillRect(head.x - quarterSize, head.y - quarterSize, eyeSize, eyeSize)
                fillRect(head.x + quarterSize, head.y - quarterSize, eyeSize, eyeSize)
            }
            Direction.LEFT -> {
                fillRect(head.x - quarterSize, head.y + quarterSize, eyeSize, eyeSize)
                fillRect(head.x - quarterSize, head.y - quarterSize, eyeSize, eyeSize)
            }
            Direction.RIGHT -> {
                fillRect(head.x + quarterSize, head.y + quarterSize, eyeSize, eyeSize)
                fillRect(head.x + quarterSize, head.y - quarterSize, eyeSize, eyeSize)
            }
        }

        fillStyle = "yellow"
        font = "12px serif"
        fillText(snake.position.size.toString(), head.x.toDouble(), head.y.toDouble() - game.blockSize)
    }
}

private fun CanvasRenderingContext2D.renderApples(game: Game) {
    val halfSize = game.blockSize / 2.0
    fillStyle = "red"
    game.apples.forEach {
        fillRect(it.position.x - halfSize, it.position.y - halfSize, 2 * halfSize, 2 * halfSize)
    }
}

private fun CanvasRenderingContext2D.renderBackground(game: Game) {
    fillStyle = "#828787"
    fillRect(0.0, 0.0, game.width.toDouble(), game.height.toDouble())
}

private fun CanvasRenderingContext2D.renderBorder(game: Game) {
    beginPath()
    lineWidth = 6.0
    strokeStyle = "black"
    rect(0.0, 0.0, game.width.toDouble(), game.height.toDouble())
    stroke()
}

private fun CanvasRenderingContext2D.withFreshContext(vararg blocks: CanvasRenderingContext2D.() -> Unit) {
    blocks.forEach { block ->
        save()
        block()
        restore()
    }
}
