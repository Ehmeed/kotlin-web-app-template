package com.ehmeed.js

import com.ehmeed.common.helloFromCommon
import com.ehmeed.common.platformValue
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window
import react.*
import react.dom.*


val dynamicType: dynamic = "Dynamic string!"

fun main() {
    println("Hello from js!")
    helloFromCommon()
    println(platformValue)
    println(dynamicType.unsafeCast<String>())
    println(js("""typeof 'a'"""))
    println("Btoa using js: "+ js("""btoa('hello');"""))
    println("Btoa using declarations: "+ window.btoa("hello"))
    println("Btoa using external fun: "+ btoa("hello"))
    println("Using npm package - " + sorted(arrayOf(1, 2, 3, 4, 5)))
    document.bgColor = "FFAA12"
    (document.getElementById("root") as HTMLDivElement).innerHTML = "Manipulating DOM!"
    render(document.getElementById("root")) {
        child(Welcome::class) {
            attrs.name = "react"
        }
    }
}


external fun btoa(string: String): String

external fun functionWithOptionalParameters(string: String, optionalInt: Int = definedExternally): Unit

@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(a: Array<T>): Boolean



external interface WelcomeProps : RProps {
    var name: String
}

class Welcome: RComponent<WelcomeProps, RState>() {
    override fun RBuilder.render() {
        div {
            +"Hello, ${props.name}"
        }
    }
}
