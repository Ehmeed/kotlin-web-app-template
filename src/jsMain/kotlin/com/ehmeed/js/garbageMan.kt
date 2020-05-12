package com.ehmeed.js

import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
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

external private fun functionWithOptionalParameters(string: String, optionalInt: Int = definedExternally): Unit

@JsModule("is-sorted")
@JsNonModule
external private fun <T> sorted(a: Array<T>): Boolean


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
