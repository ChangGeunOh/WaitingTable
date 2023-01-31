package kr.pe.paran.waiting.data.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.data.remote.Network
import kr.pe.paran.waiting.data.remote.Network.getHttpClient
import kr.pe.paran.waiting.data.remote.NetworkStatus
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.domain.model.IpAddress
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.repository.DataStoreSource
import kr.pe.paran.waiting.domain.repository.RemoteDataSource
import kr.pe.paran.waiting.presentation.screen.display.DisplayStatus
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData

class RemoteDataSourceImpl(
    private val dataStore: DataStoreSource
) : RemoteDataSource {

    lateinit var ipAddress: IpAddress

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val settingData = dataStore.loadSettingData()
            ipAddress = settingData.serverAddress
            Network.serverAddress.value = if (settingData.appMode == AppMode.CLIENT) ipAddress.getIpAddress() else ""
        }
    }

    override suspend fun saveReceiptNumberData(receiptNumberData: ReceiptNumberData) {
//        getHttpClient(ipAddress).use {
//            it.post("receiptNumber") {
//                contentType(ContentType.Application.Json)
//                setBody(receiptNumberData)
//            }
//        }
        postData(url = "receiptNumber", data = receiptNumberData)
    }

    override suspend fun loadReceiptNumberData(id: Int): ReceiptNumberData {
//        return getHttpClient(ipAddress).use {
//            it.get("receiptNumber/id/$id").body()
//        }
        return getData<ReceiptNumberData>("receiptNumber/id/$id") ?: ReceiptNumberData()
    }

    override suspend fun loadReceiptNumberData(isFirst: Boolean): ReceiptNumberData {
//        return getHttpClient(ipAddress).use {
//            it.get("receiptNumber/first/$isFirst").body()
//        }
        return getData<ReceiptNumberData>("receiptNumber/first/$isFirst") ?: ReceiptNumberData()
    }

    override suspend fun loadReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData {
//        return getHttpClient(ipAddress).use {
//            it.get("receiptNumber/status/${waitingStatus.name}").body()
//        }
        return getData<ReceiptNumberData>("receiptNumber/status/\${waitingStatus.name")
            ?: ReceiptNumberData()
    }

    override suspend fun getLastWaitingNumber(): Int {
//        return getHttpClient(ipAddress).use {
//            it.get("receiptNumber/last").body()
//        }
        return getData<Int>("receiptNumber/last") ?: 0
    }

    override suspend fun loadWaitingCount(): Int {
//        return getHttpClient(ipAddress).use {
//            it.get("waitingCount").body()
//        }
        return getData<Int>("waitingCount") ?: 0
    }

    override fun flowManagerWaitingNumber(): Flow<ManagerWaitingNumber> {
//        return flow {
//            getHttpClient(ipAddress).use {
//                while (true) {
//                    val data: ManagerWaitingNumber = it.get("managerWaiting").body()
//                    emit(data)
//                    delay(Constants.REMOTE_REPEAT_TIME)
//                }
//            }
//        }
        return flowData("managerWaiting")
    }

    override suspend fun initReceiptNumber() {
        getHttpClient(ipAddress).use {
            it.get("initReceiptNumber")
        }
    }

    override fun flowWaitingCount(): Flow<Int> {
//        return flow {
//            getHttpClient(ipAddress).use {
//                while (true) {
//                    val count: Int = it.get("waitingCount").body()
//                    emit(count)
//                    delay(Constants.REMOTE_REPEAT_TIME)
//                }
//            }
//        }
        return flowData("waitingCount")
    }

    override fun flowNumberPadData(): Flow<NumberPadData> {
//        return flow {
//            getHttpClient(ipAddress).use {
//                while (true) {
//                    val numberPadData: NumberPadData = it.get("numberPad").body()
//                    emit(numberPadData)
//                    delay(Constants.REMOTE_REPEAT_TIME)
//                }
//            }
//        }
        return flowData("numberPad")
    }

    override fun flowDisplayWaitingList(): Flow<DisplayStatus> {
//        return flow {
//            getHttpClient(ipAddress).use {
//                while (true) {
//                    val displayStatus: DisplayStatus = it.get("displayWaiting").body()
//                    emit(displayStatus)
//                    delay(Constants.REMOTE_REPEAT_TIME)
//                }
//            }
//        }
        return flowData("displayWaiting")
    }

    private inline fun <reified T> flowData(url: String): Flow<T> {
        return flow {
            getHttpClient(ipAddress).use {
                while (true) {
//                    try {
//                        val data: T = it.get(url).body()
//                        emit(data)
//                        delay(Constants.REMOTE_REPEAT_TIME)
//                    } catch (e: ResponseException) {
//                        e.printStackTrace()
//                        Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
//                    }
                    val body = getBodyData<T>(it, url)
                    body?.let { data ->
                        emit(data)
                    }
                    delay(Constants.REMOTE_REPEAT_TIME)
                }
            }
        }
    }

    private suspend inline fun <reified T> getBodyData(httpClient: HttpClient, url: String): T? {
        var data: T? = null
        try {
            data = httpClient.get(url).body()
            Network.networkStatus.value = NetworkStatus.Success
        } catch (e: ResponseException) {
            Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
            e.printStackTrace()
        } catch (e: Exception) {
            Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
            e.printStackTrace()
        }
        return data
    }

    private suspend inline fun <reified T> getData(url: String): T? {
        getHttpClient(ipAddress).use {
            return getBodyData(it, url)
        }
    }

    private suspend inline fun postData(url: String, data: Any) {
        Logcat.i("url>$url :::: ${data.toString()}")
        getHttpClient(ipAddress).use {
            try {
                it.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }
                Network.networkStatus.value = NetworkStatus.Success
            } catch (e: ResponseException) {
                Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
                e.printStackTrace()
            } catch (e: Exception) {
                Network.networkStatus.value = NetworkStatus.Error(message = e.message ?: "")
                e.printStackTrace()
            }
        }
    }
}