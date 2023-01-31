package kr.pe.paran.waiting.data.remote

import androidx.compose.runtime.mutableStateOf
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kr.pe.paran.waiting.domain.model.IpAddress

object Network {

    val networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Success)
    var serverAddress = mutableStateOf("")

    fun getHttpClient(ipAddress: IpAddress) = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url {
                protocol = ipAddress.getURLProtocol()
                host = ipAddress.host
                port = ipAddress.port
            }
        }
        install(Logging) {
            logger = Logger.ANDROID
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 2000
        }
    }

}

sealed class NetworkStatus {
    object Success : NetworkStatus()
    data class Error(val message: String = ""): NetworkStatus()
}