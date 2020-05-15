package com.ehmeed.js

import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import com.ehmeed.common.snake.Game
import com.ehmeed.common.snake.net.Command
import com.ehmeed.common.snake.serialization.jsonSerializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.dom.create
import kotlinx.html.js.canvas
import kotlinx.html.js.div
import kotlinx.html.style
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window

fun myStyle(builder: CSSBuilder.() -> Unit): String = CSSBuilder().apply(builder).toString()

@OptIn(ImplicitReflectionSerializer::class)
fun main() {
    println("Starting JS")

    val root = document.create.div() {
        style = myStyle {
            width = LinearDimension(window.innerWidth.toString())
            height = LinearDimension(window.innerHeight.toString())
            backgroundColor = Color.indianRed
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
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = 800
    context.canvas.height = 640

    root.appendChild(canvas)
    document.body!!.appendChild(root)

    GlobalScope.launch {
        client.ws(host = serverHost, port = serverPort, path = "/snake") {
            val cmd = Command.Register("new snake").serialize()
            println("sending cmd: $cmd")
            send(Frame.Text(cmd))
            println("sent command")
            for (update in incoming) {
                if (update is Frame.Text) {
                    val game = jsonSerializer.fromJson<Game>(jsonSerializer.parseJson(update.readText()))
                    println(game.snakes)
                    println(game.apples)
                }
            }
        }
    }



//    GlobalScope.launch(Dispatchers.Main) {
//        client.ws(host = serverHost, port = serverPort) {
//            println("Sending read")
//            outgoing.send(Frame.Text("read"))
//            println("sent read")
//            for (frame in incoming) {
//                println("Received frame")
//                when (frame) {
//                    is Frame.Text -> {
//                        addMessage(frame.readText())
//                    }
//                }
//            }
//        }
//    }
//
//    val button = document.createElement("button")
//    button.innerHTML = "Send"
//    button.addEventListener("click", {
//        GlobalScope.launch(Dispatchers.Main) {
//            client.ws(host = serverHost, port = serverPort) {
//                val value = document.getElementById("input_text")?.value() ?: ""
//                outgoing.send(Frame.Text(value))
//            }
//        }
//    })
//    document.getElementById("root")?.appendChild(button)
//
//    val input = document.createElement("input")
//    input.id = "input_text"
//    document.getElementById("root")?.appendChild(input)

}

val client = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(WebSockets)
}
