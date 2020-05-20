package com.ehmeed.js

import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.domain.Direction
import com.ehmeed.common.snake.net.Command
import com.ehmeed.common.snake.serialization.jsonSerializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.dom.create
import kotlinx.html.js.canvas
import kotlinx.html.js.div
import kotlinx.html.style
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.random.Random

fun myStyle(builder: CSSBuilder.() -> Unit): String = CSSBuilder().apply(builder).toString()

val snakeId = List(20) { ('a'..'z').random() }.joinToString(separator = "")
private val commands = Channel<Command>(Channel.CONFLATED)

val gameId
    get() = if(Random.nextBoolean()) "todo" else "haha"

@OptIn(ImplicitReflectionSerializer::class)
fun main() {
    println("Starting JS")

    val root = document.create.div() {
        style = myStyle {
            width = LinearDimension(window.innerWidth.toString())
            height = LinearDimension(window.innerHeight.toString())
            backgroundColor = Color("#48a4ab")
            border = "2px solid black"
            position = Position.absolute
        }
    }

    val canvas = document.create.canvas {
        myStyle {
            width = 800.px
            height = 640.px
        }
    }
    val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D
    canvasContext.canvas.width = 800
    canvasContext.canvas.height = 640

    root.appendChild(canvas)
    document.body!!.appendChild(root)

    registerControls()

    GlobalScope.launch {
        client.ws(host = serverHost, port = serverPort, path = "/snake") {
            launch {
                canvasContext.renderGetReadyMessage()
                delay(3000)
                send(Frame.Text(Command.Register(snakeId, gameId).serialize()))
            }
            launch {
                for (cmd in commands) send(Frame.Text(cmd.serialize()))
            }
            delay(3500)
            for (update in incoming) {
                if (update is Frame.Text) {
                    val game = jsonSerializer.fromJson<Game>(jsonSerializer.parseJson(update.readText()))
                    println(game)
                    canvasContext.render(game)
                }
            }
        }
    }
}

fun registerControls() {
    document.addEventListener("keydown", {
        val event = it.unsafeCast<KeyboardEvent>()
        val direction = when (event.keyCode.asKeyCode()) {
            KeyCode.DOWN -> Direction.DOWN
            KeyCode.UP -> Direction.UP
            KeyCode.LEFT -> Direction.LEFT
            KeyCode.RIGHT -> Direction.RIGHT
            KeyCode.Q -> {
                null
            }
            KeyCode.IGNORED -> null
        }
        if (direction != null) {
            event.preventDefault()
            GlobalScope.launch {
                commands.send(Command.Turn(snakeId, gameId, direction))
            }
        }
    })
}

@OptIn(KtorExperimentalAPI::class)
val client = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(WebSockets)
}
