package kr.pe.paran.waiting.presentation.screen.setting

import kr.pe.paran.waiting.domain.model.*

data class SettingState(
    var menuList: List<MenuItem> = emptyList(),
    var privateAddress: String = "",
    var publicAddress: String = "",
    var serverAddress: IpAddress = IpAddress(),
    var appMode: AppMode = AppMode.SERVER,
    var screenMode: ScreenMode = ScreenMode.WAITING,
    var port: String = ""
) {
    fun getSettingData() = SettingData(
        appMode = appMode,
        screenMode = screenMode,
        serverAddress = serverAddress,
        port = if (port.isEmpty()) 2842 else port.toInt()
    )

    fun loadPrivateAddress() = "$privateAddress:$port"
    fun loadPublicAddress() = "$publicAddress:$port"

}
