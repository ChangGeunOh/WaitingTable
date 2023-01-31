package kr.pe.paran.waiting.domain.model

enum class NetworkType {
    NONE, WIFI, CELLULAR, ETHERNET
}

data class NetworkInfo(
    var isAvailable: Boolean = false,
    var type: NetworkType = NetworkType.NONE,
    var ssid: String = "",
    var strength: Int = 0,
    var deviceAddress: String = "",
    var serverAddress: String = "",
    var isLiveServer: Boolean = true
)
