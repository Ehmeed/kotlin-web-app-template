package com.ehmeed.js.render.game

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Apple
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Image
import kotlin.browser.document

private val RED_TOMATO = document.getElementById("red_tomato").unsafeCast<Image>()
private val RED_MUSHROOM = document.getElementById("red_mushroom").unsafeCast<Image>()

fun CanvasRenderingContext2D.renderApples(game: Game) {
    val halfSize = game.blockSize / 2.0
    val adjust = game.blockSize
    game.apples.forEach {
        val image = when (it) {
            is Apple.RedTomato -> RED_TOMATO
            is Apple.RedMushroom -> RED_MUSHROOM
        }
        drawImage(image, it.position.x + adjust - halfSize, it.position.y + adjust - halfSize, 2 * halfSize, 2 * halfSize)
    }
}
