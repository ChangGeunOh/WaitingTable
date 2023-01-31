package kr.pe.paran.waiting.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.presentation.screen.display.DisplayStatus
import kr.pe.paran.waiting.presentation.screen.manager.contents.ManagerWaitingNumber
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData

interface LocalDataSource {
    suspend fun saveReceiptNumberData(receiptNumberData: ReceiptNumberData)
    suspend fun loadReceiptNumberData(id: Int): ReceiptNumberData
    suspend fun loadReceiptNumberData(isFirst: Boolean): ReceiptNumberData
    suspend fun loadReceiptNumberData(waitingStatus: WaitingStatus): ReceiptNumberData

    suspend fun getLastWaitingNumber(): Int
    fun flowWaitingCount(): Flow<Int>
    fun flowCalledWaitingData(): Flow<ReceiptNumberData>
    fun flowDisplayWaitingList() : Flow<DisplayStatus>
    fun flowManagerWaitingNumber(): Flow<ManagerWaitingNumber>

    suspend fun initReceiptNumber()
    fun flowNumberPadData(): Flow<NumberPadData>
}