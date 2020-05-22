package com.ehmeed.js.render

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.domain.Position
import com.ehmeed.common.snake.domain.Snake
import com.ehmeed.js.snakeId
import org.w3c.dom.CanvasRenderingContext2D

fun CanvasRenderingContext2D.renderSnakes(game: Game) {
    val halfSize = game.blockSize / 2.0
    val quarterSize = game.blockSize / 4.0
    val adjust = game.blockSize
    for (snake in game.snakes) {
        val head = snake.head()
        snakeBody(snake, adjust, halfSize)
        snakeEyes(game, snake, head, adjust, quarterSize)
        snakeScore(snake, head, adjust, game)
    }
}

private fun CanvasRenderingContext2D.snakeScore(snake: Snake, head: Position, adjust: Int, game: Game) {
    fillStyle = "yellow"
    font = "12px serif"
    fillText(snake.score.toString(), head.x.toDouble() + adjust, head.y.toDouble() + adjust - .75 * game.blockSize)
}

private fun CanvasRenderingContext2D.snakeEyes(game: Game, snake: Snake, head: Position, adjust: Int, quarterSize: Double) {
    val eyeSize = game.blockSize / 8.0
    fillStyle = if (snake.id == snakeId) "black" else "red"
    when (snake.direction) {
        Direction.UP -> {
            fillRect(head.x + adjust - quarterSize, head.y + adjust + quarterSize, eyeSize, eyeSize)
            fillRect(head.x + adjust + quarterSize, head.y + adjust + quarterSize, eyeSize, eyeSize)
        }
        Direction.DOWN -> {
            fillRect(head.x + adjust - quarterSize, head.y + adjust - quarterSize, eyeSize, eyeSize)
            fillRect(head.x + adjust + quarterSize, head.y + adjust - quarterSize, eyeSize, eyeSize)
        }
        Direction.LEFT -> {
            fillRect(head.x + adjust - quarterSize, head.y + adjust + quarterSize, eyeSize, eyeSize)
            fillRect(head.x + adjust - quarterSize, head.y + adjust - quarterSize, eyeSize, eyeSize)
        }
        Direction.RIGHT -> {
            fillRect(head.x + adjust + quarterSize, head.y + adjust + quarterSize, eyeSize, eyeSize)
            fillRect(head.x + adjust + quarterSize, head.y + adjust - quarterSize, eyeSize, eyeSize)
        }
    }
}

private fun CanvasRenderingContext2D.snakeBody(snake: Snake, adjust: Int, halfSize: Double) {
    fillStyle = if (snake.id == snakeId) "green" else "black"
    snake.position.forEach {
        fillRect(it.x + adjust - halfSize, it.y + adjust - halfSize, 2 * halfSize, 2 * halfSize)
    }
}
