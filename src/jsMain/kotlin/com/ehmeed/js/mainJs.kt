package com.ehmeed.js

import com.ehmeed.js.component.AppComponent
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.WebSockets
import react.dom.render
import kotlin.browser.document

fun main() {
    println("Starting JS")
    render(document.getElementById("root")) {
        child(AppComponent::class) {}
    }
}

val client = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(WebSockets)
}
