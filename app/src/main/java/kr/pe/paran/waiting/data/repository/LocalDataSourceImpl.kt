package kr.pe.paran.waiting.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.data.local.database.LocalDatabase
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.repository.LocalDataSource
import kr.pe.paran.waiting.presentation.screen.display.DisplayStatus
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(localDatabase: LocalDatabase) : LocalDataSource {

    private val receiptDao = localDatabase.receiptDao()

    override suspend fun saveReceiptNumberData(receiptNumberData: ReceiptNumberData) {
        Logcat.i("saveReceiptNumberData>${receiptNumberData.toString()}")
        receiptDao.insertReceiptNumberData(receiptNumberData = receiptNumberData)
    }

    override suspend fun loadReceiptNumberData(id: Int): ReceiptNumberData {
        return receiptDao.getReceiptNumberData(id = id) ?: ReceiptNumberData()
    }

    override suspend fun loadReceiptNumberData(isFirst: Boolean): ReceiptNumberData {
        return if (isFirst) receiptDao.getFirstReceiptNumberData()
            ?: ReceiptNumberData() else receiptDao.getLastReceiptNumberData()
    }

    override suspend fun loadReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData {
        return receiptDao.getReceiptNumberData(waitingStatus = waitingStatus) ?: ReceiptNumberData()

    }

    override suspend fun getLastWaitingNumber(): Int {
        return receiptDao.getLastWaitingNumber() ?: 1
    }

    override fun flowWaitingCount(): Flow<Int> {
        return receiptDao.flowWaitingCount()
    }

    override fun flowCalledWaitingData(): Flow<ReceiptNumberData> {
        return flow {
            receiptDao.flowCalledWaitingNumber().collect {
                it?.let {
                    emit(it)
                }
            }
        }
    }

    override fun flowDisplayWaitingList(): Flow<DisplayStatus> {
        Logcat.i("flowDisplayWaitingList")
        return flow {

            // emit Init
            var callReceiptNumberData =
                receiptDao.getReceiptNumberData(waitingStatus = WaitingStatus.CALLED)
            if (callReceiptNumberData == null) {
                callReceiptNumberData =
                    receiptDao.getFirstReceiptNumberData(waitingStatus = WaitingStatus.CALLING)
                        ?: ReceiptNumberData()
            }
            var receiptNumberList: List<ReceiptNumberData> = emptyList()

            receiptNumberList = if (callReceiptNumberData.waitingStatus == WaitingStatus.NONE) {
                receiptDao.getFirstReceiptNumberList()
            } else {
                receiptDao.getDisplayWaitingDataList(callReceiptNumberData.createdDate)
            }
            var countWaiting = receiptDao.getCountWaitingStatus(waitingStatus = WaitingStatus.WAIT)
            val displayStatus = DisplayStatus(
                doneReceiptNumberList = receiptNumberList.filter { it.waitingStatus == WaitingStatus.DONE },
                waitReceiptNumberList = receiptNumberList.filter { it.waitingStatus == WaitingStatus.WAIT },
                callReceiptNumberData = callReceiptNumberData,
                countWaiting = countWaiting
            )

            emit(displayStatus)

            receiptDao.flowCalledWaitingNumber().collect { receiptNumberData ->
                receiptNumberData?.let {
                    Logcat.i("+++++${it.toString()}")
                    receiptNumberList =
                        receiptDao.getDisplayWaitingDataList(receiptNumberData.createdDate)
                    countWaiting =
                        receiptDao.getCountWaitingStatus(waitingStatus = WaitingStatus.WAIT)
                    emit(
                        DisplayStatus(
                            doneReceiptNumberList = receiptNumberList.filter { it.waitingStatus == WaitingStatus.DONE },
                            waitReceiptNumberList = receiptNumberList.filter { it.waitingStatus == WaitingStatus.WAIT },
                            callReceiptNumberData = receiptNumberData,
                            countWaiting = countWaiting
                        )
                    )
                }
            }
        }
    }

    override fun flowManagerWaitingNumber(): Flow<ManagerWaitingNumber> {

        return flow {

            while (true) {
                var currentReceiptNumberData =
                    receiptDao.getReceiptNumberData(waitingStatus = WaitingStatus.CALLED)

                if (currentReceiptNumberData == null) {
                    currentReceiptNumberData = receiptDao.getReceiptNumberData(waitingStatus = WaitingStatus.CALLING)
                        ?: ReceiptNumberData()
                }

                val nextReceiptNumberData =
                    receiptDao.getFirstReceiptNumberData() ?: ReceiptNumberData()
                val count = receiptDao.getCountWaitingStatus()
                val managerWaitingNumber = ManagerWaitingNumber(
                    currentReceiptNumberData = currentReceiptNumberData,
                    nextReceiptNumberData = nextReceiptNumberData,
                    countWaiting = count
                )
                Logcat.i(managerWaitingNumber.toString())
                emit(managerWaitingNumber)
                delay(500)
            }

//            val currentReceiptNumberData =
//                receiptDao.getReceiptNumberData(waitingStatus = WaitingStatus.CALL)
//                    ?: ReceiptNumberData()
//            val nextReceiptNumberData =
//                receiptDao.getFirstReceiptNumberData() ?: ReceiptNumberData()
//            val count = receiptDao.getCountWaitingStatus()
//            val managerWaitingNumber = ManagerWaitingNumber(
//                currentReceiptNumberData = currentReceiptNumberData,
//                nextReceiptNumberData = nextReceiptNumberData,
//                countWaiting = count
//            )
//            emit(managerWaitingNumber)
//
//            receiptDao.flowCalledNoneWaitingNumber().collect { receiptNumberData ->
//                try {
//                    receiptNumberData?.let {
//                        val nextReceiptNumber =
//                            receiptDao.getNextReceiptNumberData(it.createdDate)
//                                ?: ReceiptNumberData()
//                        val countWaiting = receiptDao.getCountWaitingStatus()
//                        val data = ManagerWaitingNumber(
//                            currentReceiptNumberData = it,
//                            nextReceiptNumberData = nextReceiptNumber,
//                            countWaiting = countWaiting
//                        )
//                        Logcat.i("ManagerWaitingNumber>$data")
//                        emit(data)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
        }
    }

    override suspend fun initReceiptNumber() {
        receiptDao.initReceiptNumber(
            fromWaitingStatus = WaitingStatus.CALLING,
            toWaitingStatus = WaitingStatus.DONE
        )
        receiptDao.initReceiptNumber(
            fromWaitingStatus = WaitingStatus.WAIT,
            toWaitingStatus = WaitingStatus.CANCEL
        )
        receiptDao.insertReceiptNumberData(ReceiptNumberData(waitingStatus = WaitingStatus.RESET))
    }

    override fun flowNumberPadData(): Flow<NumberPadData> {
        var modifiedDate = 0L
        return flow {
            Logcat.i("flowNumberPadData")
            while (true) {
                val newModifiedDate = receiptDao.flowDetectionChange()
                Logcat.i("modified>$modifiedDate :: $newModifiedDate")
                if (newModifiedDate != modifiedDate) {
                    val countWaiting = receiptDao.getWaitingCount()
                    val calledReceiptNumberData =
                        receiptDao.getReceiptNumberData(waitingStatus = WaitingStatus.CALLING)
                            ?: ReceiptNumberData()
                    emit(
                        NumberPadData(
                            countWaiting = countWaiting,
                            receiptNumberData = calledReceiptNumberData
                        )
                    )
                    modifiedDate = newModifiedDate
                }
                delay(500)
            }
        }
    }
}