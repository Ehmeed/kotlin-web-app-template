package com.ehmeed.js

import com.ehmeed.common.helloFromCommon
import com.ehmeed.common.platformValue
import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.*
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.position
import kotlinx.css.zIndex
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div
import react.dom.render
import styled.StyleSheet
import styled.css
import styled.styledDiv
import kotlin.browser.document
import kotlin.browser.window


val dynamicType: dynamic = "Dynamic string!"

fun main() {
    println("Hello from js!")
    helloFromCommon()
    println(platformValue)
    println(dynamicType.unsafeCast<String>())
    println(js("""typeof 'a'"""))
    println("Btoa using js: " + js("""btoa('hello');"""))
    println("Btoa using declarations: " + window.btoa("hello"))
    println("Btoa using external fun: " + btoa("hello"))
    println("Using npm package - " + sorted(arrayOf(1, 2, 3, 4, 5)))
    document.bgColor = "FFAA12"
    (document.getElementById("root") as HTMLDivElement).innerHTML = "Manipulating DOM!"
    render(document.getElementById("root")) {
        child(Welcome::class) {
            attrs.name = "react"
        }
    }
}

fun initalizeCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = window.innerWidth
    context.canvas.height = window.innerHeight
    document.body!!.appendChild(canvas)

    context.save()
    context.moveTo(10.0, 10.0)
    context.lineWidth = 20.0
    context.bezierCurveTo(20.0, 30.0, 40.0, 50.0, 10.0, 10.0)
    context.stroke()
    context.restore()
    return canvas
}


external fun btoa(string: String): String

external fun functionWithOptionalParameters(string: String, optionalInt: Int = definedExternally): Unit

@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(a: Array<T>): Boolean


interface WelcomeProps : RProps {
    var name: String
}

interface WelcomeState : RState {
    var list: List<List<Int>>?
}

class Welcome : RComponent<WelcomeProps, WelcomeState>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +Styles.styles }
            +"Hello, ${props.name}"
            state.list?.forEach {
                div {
                    +it.toString()
                }
            }
        }
        initalizeCanvas()
    }

    override fun WelcomeState.init() {
        val mainScope = MainScope()

        mainScope.launch {
            val videos = fetchVideoCor()
            setState { list = videos }
        }
    }
}

object Styles : StyleSheet("Styles", isStatic = true) {
    val styles by css {
        position = kotlinx.css.Position.absolute
        backgroundColor = Color.green
        zIndex = 2
    }
}

suspend fun fetchVideo(id: Int): List<Int> =
    window.fetch("http://$serverHost:$serverPort/data")
        .await()
        .json()
        .await()
        .unsafeCast<List<Int>>()

suspend fun fetchVideos(): List<List<Int>> = coroutineScope {
    (1..25).map { id ->
        async { fetchVideo(id) }
    }.awaitAll()
}

val httpClient = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun fetchVideoCor(): List<List<Int>> {
    return coroutineScope {
        (1..25).map {
            async {
                httpClient.get<List<Int>>(url = Url("http://$serverHost:$serverPort/data"))
            }
        }.awaitAll()
    }
}
