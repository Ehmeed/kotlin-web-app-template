package com.ehmeed.js.render

import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.js.snakeId
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
