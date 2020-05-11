package com.ehmeed.js

import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import io.ktor.client.HttpClient
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


private val dynamicType: dynamic = "Dynamic string!"

private fun maina() {
    println("Hello from js!")
//    helloFromCommon()
    println(dynamicType.unsafeCast<String>())
    println(js("""typeof 'a'"""))
    println("Btoa using js: " + js("""btoa('hello');"""))
    println("Btoa using declarations: " + window.btoa("hello"))
    println("Btoa using external private fun: " + btoa("hello"))
    println("Using npm package - " + sorted(arrayOf(1, 2, 3, 4, 5)))
    document.bgColor = "FFAA12"
    (document.getElementById("root") as HTMLDivElement).innerHTML = "Manipulating DOM!"
    render(document.getElementById("root")) {
        child(Welcome::class) {
            attrs.name = "react"
        }
    }
}

private fun initalizeCanvas(): HTMLCanvasElement {
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


external private fun btoa(string: String): String

external private fun  functionWithOptionalParameters(string: String, optionalInt: Int = definedExternally): Unit

@JsModule("is-sorted")
@JsNonModule
external private fun <T> sorted(a: Array<T>): Boolean


private interface WelcomeProps : RProps {
    var name: String
}

private interface WelcomeState : RState {
    var list: List<List<Int>>?
}

private class Welcome : RComponent<WelcomeProps, WelcomeState>() {
    override  fun RBuilder.render() {
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

    override  fun WelcomeState.init() {
         val mainScope = MainScope()

        mainScope.launch {
             val videos = fetchVideoCor()
             setState { list = videos }
        }
    }
}

private object Styles : StyleSheet("Styles", isStatic = true) {
     val styles by css {
        position = kotlinx.css.Position.absolute
        backgroundColor = Color.green
        zIndex = 2
    }
}

suspend private fun fetchVideo(id: Int): List<Int> =
    window.fetch("http://$serverHost:$serverPort/data")
        .await()
        .json()
        .await()
        .unsafeCast<List<Int>>()

suspend private fun fetchVideos(): List<List<Int>> = coroutineScope {
    (1..25).map { id ->
        async { fetchVideo(id) }
    }.awaitAll()
}

private val httpClient = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend private fun fetchVideoCor(): List<List<Int>> {
    return coroutineScope {
        (1..25).map {
            async {
                httpClient.get<List<Int>>(url = Url("http://$serverHost:$serverPort/data"))
            }
        }.awaitAll()
    }
}
