package kr.pe.paran.waiting.domain.model

import io.ktor.http.*

@kotlinx.serialization.Serializable
data class IpAddress(
    val protocol: String = "http",
    var host: String = "",
    val port: Int = 2842,
) {


    fun getIpAddress() = "$host:$port"

    fun getURLProtocol(): URLProtocol {
        return if (protocol == "http") URLProtocol.HTTP else URLProtocol.HTTPS
    }

}