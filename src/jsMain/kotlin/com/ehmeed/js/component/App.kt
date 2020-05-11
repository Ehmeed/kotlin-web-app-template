package com.ehmeed.js.component

import com.ehmeed.common.serverHost
import com.ehmeed.common.serverPort
import com.ehmeed.js.client
import com.ehmeed.js.value
import io.ktor.client.features.websocket.ws
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.zIndex
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import react.*
import styled.*

interface AppProps : RProps {

}

interface AppState : RState {
    var messages: List<String>
    var message: String?
}

class AppComponent(prps: AppProps) : RComponent<AppProps, AppState>(prps) {
    override fun RBuilder.render() {
        styledInput(type = InputType.text) {
            this.attrs.onChangeFunction = {
                val value = it.target?.value()
                setState { message = value }
            }
            this.attrs.value = state.message ?: ""
            this.attrs.placeholder = "chat here..."
        }
        styledButton {
            this.attrs.onClickFunction = {
                val msg = state.message ?: ""
                setState { message = null }
                val mainScope = MainScope()
                mainScope.launch {
                    client.ws(host = serverHost, port = serverPort) {
                        outgoing.send(Frame.Text(msg))
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    setState { messages = listOf(frame.readText()) + this.messages }
                                }
                            }
                        }
                    }
                }
            }
        }
        state.messages.map {
            styledDiv {
                css { +AppStyles.styles }
                +it
            }
        }
    }


    override fun AppState.init(props: AppProps) {
        messages = listOf()

//        val mainScope = MainScope()
//        mainScope.launch {
//            client.ws(host = serverHost, port = serverPort) {
//                for (frame in incoming) {
//                    when (frame) {
//                        is Frame.Text -> {
//                            setState { messages = listOf(frame.readText()) + this.messages }
//                        }
//                    }
//                }
//            }
//        }
    }
}

private object AppStyles : StyleSheet("AppStyles", isStatic = true) {
    val styles by css {
        backgroundColor = Color.green
        zIndex = 2
    }
}
