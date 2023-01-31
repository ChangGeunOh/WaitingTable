package kr.pe.paran.waiting.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.*
import kr.pe.paran.waiting.domain.repository.DataStoreSource
import kr.pe.paran.waiting.domain.repository.LocalDataSource
import kr.pe.paran.waiting.domain.repository.RemoteDataSource
import kr.pe.paran.waiting.presentation.screen.display.DisplayStatus
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData
import javax.inject.Inject

class Repository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dataStore: DataStoreSource,
) {

    private val maxWaitingNumber = 999
    private var isServerMode: Boolean = false

    suspend fun loadReceiptNumberData(id: Int): ReceiptNumberData {
        return if (isServerMode) {
            local.loadReceiptNumberData(id = id)
        } else {
            remote.loadReceiptNumberData(id = id)
        }
    }

    suspend fun loadReceiptNumberData(isFirst: Boolean): ReceiptNumberData {
        return if (isServerMode) {
            local.loadReceiptNumberData(isFirst = isFirst)
        } else {
            remote.loadReceiptNumberData(isFirst = isFirst)
        }
    }

    suspend fun loadReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData {
        return if (isServerMode) {
            local.loadReceiptNumberData(waitingStatus = waitingStatus)
        } else {
            remote.loadReceiptNumberData(waitingStatus = waitingStatus) ?: ReceiptNumberData()
        }
    }


    suspend fun saveReceiptNumberData(receiptNumberData: ReceiptNumberData): String {
        if (receiptNumberData.waitingStatus == WaitingStatus.WAIT) {
            receiptNumberData.waitingNumber = loadLastNumber()
        }
        if (isServerMode) {
            local.saveReceiptNumberData(receiptNumberData = receiptNumberData)
        } else {
            remote.saveReceiptNumberData(receiptNumberData = receiptNumberData)
        }
        return getWaitingNumberFormat(receiptNumberData.waitingNumber)
    }

    suspend fun loadLastNumber(): Int {
        var number = if (isServerMode) {
            local.getLastWaitingNumber()
        } else {
            remote.getLastWaitingNumber()
        }
        if (number > maxWaitingNumber) {
            number = 1
        }
        return number
    }

    private fun getWaitingNumberFormat(number: Int): String {
        val length = maxWaitingNumber.toString().length
        return String.format("%0${length}d", number)
    }

    fun flowWaitingCount(): Flow<Int> {
        Logcat.i("flowWaitingCount>$isServerMode :::")
        val countFlow = if (isServerMode) {
            local.flowWaitingCount()
        } else {
            remote.flowWaitingCount()
        }

        return countFlow
    }


    suspend fun saveSeverIp(ipAddress: String) {
        dataStore.saveServerIp(ipAddress = ipAddress)
    }

    suspend fun loadServerIp(): String {
        return dataStore.loadServerIp()
    }

    suspend fun loadModeType(): Int {
        return dataStore.loadModeType()
    }

    suspend fun saveModeType(modeType: Int) {
        dataStore.saveModeType(modeType = modeType)
    }

    suspend fun loadScreenMode(): ScreenMode {
        return dataStore.loadScreenMode()
    }

    suspend fun saveScreenMode(screenMode: ScreenMode) {
        dataStore.saveScreenMode(screenMode = screenMode)
    }


    fun flowDisplayStatus(): Flow<DisplayStatus> {
        Logcat.i("flowDisplayStatus")
        return if (isServerMode) local.flowDisplayWaitingList() else remote.flowDisplayWaitingList()
//        return if (isServerMode) flow {
//            while (true) {
//                emit(2)
//                delay((2000))
//            } else {
//            flow<List<ReceiptNumberData>> {
//
//            }
//        }
//        }

    }

    fun flowManagerWaiting(): Flow<ManagerWaitingNumber> {
        Logcat.i("flowManagerWaiting>${isServerMode}")
        return if (isServerMode) local.flowManagerWaitingNumber() else remote.flowManagerWaitingNumber()
    }

    suspend fun initReceiptNumber() {
        if (isServerMode) local.initReceiptNumber() else remote.initReceiptNumber()
    }

    suspend fun loadSettingData(): SettingData {
        return dataStore.loadSettingData()
    }

    suspend fun saveSettingData(settingData: SettingData) {
        dataStore.saveSettingData(settingData)
    }

    fun flowNumberPadData(): Flow<NumberPadData> {
        return if (isServerMode) {
            local.flowNumberPadData()
        } else {
            remote.flowNumberPadData()
        }
    }

    suspend fun loadDisplayImages(): List<String> {
        return dataStore.loadDisplayImages()
    }

    suspend fun saveDisplayImages(imageList: List<String>) {
        return dataStore.saveDisplayImages(imageList)
    }


    init {
        CoroutineScope(Dispatchers.Default).launch {
            isServerMode = dataStore.loadSettingData().appMode == AppMode.SERVER
            Logcat.i("flowWaitingCount>$isServerMode :::")
        }
    }

}