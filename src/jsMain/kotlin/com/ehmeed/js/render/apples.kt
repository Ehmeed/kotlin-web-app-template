package com.ehmeed.js.render

import com.ehmeed.common.snake.Game
import org.w3c.dom.CanvasRenderingContext2D


fun CanvasRenderingContext2D.renderApples(game: Game) {
    val halfSize = game.blockSize / 2.0
    val adjust = game.blockSize
    fillStyle = "red"
    game.apples.forEach {
        fillRect(it.position.x + adjust - halfSize, it.position.y + adjust - halfSize, 2 * halfSize, 2 * halfSize)
    }
}
