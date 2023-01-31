package kr.pe.paran.waiting.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.domain.model.ScreenMode
import kr.pe.paran.waiting.domain.model.SettingData
import kr.pe.paran.waiting.domain.repository.DataStoreSource


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "genie_store_store")

class DataStoreSourceImpl(context: Context): DataStoreSource {

    private val dataStore = context.dataStore

    private object PreferenceKey {
        val serverIpAddress = stringPreferencesKey("server_ip_address")
        val maxWaitingNumber = intPreferencesKey("waiting_max_number")
        val modeType = intPreferencesKey("mode_type")
        val screenMode = stringPreferencesKey("screen_mode")
        val settingData = stringPreferencesKey("setting_data")
        val displayImages = stringPreferencesKey("display_images")
    }

    override suspend fun getMaxWaitingNumber(): Int {
        return dataStore.data.map {
            it[PreferenceKey.maxWaitingNumber]
        }.firstOrNull() ?: Constants.WAITING_MAX_NUMBER_DEFAULT
    }

    override suspend fun setMaxWaitingNumber(maxNumber: Int) {
        dataStore.edit {
            it[PreferenceKey.maxWaitingNumber] = maxNumber
        }
    }

    override suspend fun saveServerIp(ipAddress: String) {
        dataStore.edit {
            it[PreferenceKey.serverIpAddress] = ipAddress
        }

    }

    override suspend fun loadModeType(): Int {
        return dataStore.data.map {
            it[PreferenceKey.modeType]
        }.firstOrNull() ?: 0
    }

    override suspend fun saveModeType(modeType: Int) {
        dataStore.edit {
            it[PreferenceKey.modeType] = modeType
        }
    }

    override suspend fun loadServerIp(): String {
        return dataStore.data.map {
            it[PreferenceKey.serverIpAddress]
        }.firstOrNull() ?: ""
    }

    override suspend fun loadScreenMode(): ScreenMode {

        val name = dataStore.data.map {
            it[PreferenceKey.screenMode]
        }.firstOrNull() ?: ScreenMode.WAITING.name

        return ScreenMode.valueOf(name)
    }

    override suspend fun saveScreenMode(screenMode: ScreenMode) {
        dataStore.edit {
            it[PreferenceKey.screenMode] = screenMode.name
        }
    }

    override suspend fun loadSettingData(): SettingData {
        val data = dataStore.data.map {
            it[PreferenceKey.settingData]
        }.firstOrNull()
        return if (data == null) SettingData() else Json.decodeFromString(data)
    }

    override suspend fun saveSettingData(settingData: SettingData) {
        dataStore.edit {
            val data = Json.encodeToString(settingData)
            it[PreferenceKey.settingData] = data
        }
    }

    override suspend fun loadDisplayImages(): List<String> {
        val data = dataStore.data.map {
            it[PreferenceKey.displayImages]
        }.firstOrNull()

        return if (data == null) emptyList() else Json.decodeFromString(data)
    }

    override suspend fun saveDisplayImages(imageList: List<String>) {
        val data = Json.encodeToString(imageList)
        dataStore.edit {
            it[PreferenceKey.displayImages] = data
        }
    }
}