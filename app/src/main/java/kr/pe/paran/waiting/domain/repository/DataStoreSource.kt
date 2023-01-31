package kr.pe.paran.waiting.domain.repository

import kr.pe.paran.waiting.domain.model.ScreenMode
import kr.pe.paran.waiting.domain.model.SettingData

interface DataStoreSource {
    suspend fun getMaxWaitingNumber(): Int
    suspend fun setMaxWaitingNumber(maxNumber: Int)
    suspend fun saveServerIp(ipAddress: String)
    suspend fun loadModeType(): Int
    suspend fun saveModeType(modeType: Int)
    suspend fun loadServerIp(): String
    suspend fun loadScreenMode(): ScreenMode
    suspend fun saveScreenMode(screenMode: ScreenMode)
    suspend fun loadSettingData(): SettingData
    suspend fun saveSettingData(settingData: SettingData)
    suspend fun loadDisplayImages(): List<String>
    suspend fun saveDisplayImages(imageList: List<String>)
}
