package com.ehmeed.js.render.game

import com.ehmeed.common.snake.Game
import org.w3c.dom.CanvasRenderingContext2D


fun CanvasRenderingContext2D.render(game: Game) = withFreshContext(
    { renderBackground(game) },
    { renderBorder(game) },
    { renderSnakes(game) },
    { renderApples(game) }
)

private fun CanvasRenderingContext2D.withFreshContext(vararg blocks: CanvasRenderingContext2D.() -> Unit) {
    blocks.forEach { block ->
        save()
        block()
        restore()
    }
}
