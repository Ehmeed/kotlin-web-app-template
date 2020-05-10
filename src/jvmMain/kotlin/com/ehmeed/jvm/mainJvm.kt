package com.ehmeed.jvm

import com.ehmeed.common.*
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.img
import kotlin.random.Random

fun main() {
    println("Hello from jvm!")
    helloFromCommon()
    println(platformValue)

    val server = embeddedServer(
        Netty,
        host = serverHost,
        port = serverPort,
        watchPaths = listOf("kotlin-web-app-template"),
        module = Application::Module
    )
    server.start(wait = true)
}

fun Application.Module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {}
    }
    install(CORS) {
        HttpMethod.DefaultMethods.map { method(it) }
        allowNonSimpleContentTypes = true
        anyHost()
    }
    install(Routing) {
        get("/") {
            call.respondText("Hello")
        }
        get("/data") {
            call.respond(aaa)
        }
    }
}
