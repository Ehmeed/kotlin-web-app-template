package com.ehmeed.js.render

import com.ehmeed.common.snake.Game
import org.w3c.dom.CanvasRenderingContext2D

fun CanvasRenderingContext2D.renderGetReadyMessage() {
    fillStyle = "yellow"
    font = "48px serif"
    fillText("Get ready!", 400.toDouble(), 320.toDouble())
}

fun CanvasRenderingContext2D.renderBackground(game: Game) {
    fillStyle = "#828787"
    val adjust = game.blockSize
    fillRect(0.0, 0.0, game.width.toDouble() + adjust, game.height.toDouble() + adjust)
}

fun CanvasRenderingContext2D.renderBorder(game: Game) {
    beginPath()
    val adjust = game.blockSize
    lineWidth = game.blockSize.toDouble()
    strokeStyle = "black"
    rect(0.0, 0.0, game.width.toDouble() + adjust, game.height.toDouble() + adjust)
    stroke()
}
