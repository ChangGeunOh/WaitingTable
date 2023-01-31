package kr.pe.paran.waiting.domain.model

@kotlinx.serialization.Serializable
data class SettingData(
    val appMode: AppMode = AppMode.SERVER,
    val screenMode: ScreenMode = ScreenMode.NONE,
    val serverAddress: IpAddress = IpAddress(),
    val port: Int = 2842
)