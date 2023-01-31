package kr.pe.paran.waiting.ktor.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kr.pe.paran.waiting.common.utils.Logcat

class Client {

    private val client = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private var socketSession: DefaultClientWebSocketSession? = null

    suspend fun connect(
        method: HttpMethod = HttpMethod.Get,
        host: String = "demo.piesocket.com",
        port: Int = 80,
        path: String = "/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"
    ) {
        isConnected.emit(true)
        client.webSocket(
            method = method,
            host = host,
            port = port,
            path = path
        ) {
            Logcat.i("connect")
            socketSession = this
            while (true) {
                val message = incoming.receive() as? Frame.Text
                Logcat.i(message.toString())
                message?.let {
                    val text = message.readText()
                    Logcat.i(text)
                    receivedData.emit(text)
                }
            }
        }
    }

    suspend fun sendMessage(message: Any) {
        when (message) {
            is String -> {
                Logcat.i("SendMessage: $message")
                socketSession?.send(content = message)
            }
            is Serializable -> {
                socketSession?.sendSerialized(data = message)
            }
            else -> {
                socketSession?.send(content = message.toString())
            }
        }
    }

    suspend fun disconnect() {
        client.close()
        isConnected.emit(false)
    }

    companion object {
        val isConnected = MutableSharedFlow<Boolean>(0)
        val receivedData = MutableSharedFlow<String>(0)
    }
}

/*
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/echo") {
            while(true) {
                val othersMessage = incoming.receive() as? Frame.Text
                println(othersMessage?.readText())
                val myMessage = Scanner(System.`in`).next()
                if(myMessage != null) {
                    send(myMessage)
                }
            }
        }

 */