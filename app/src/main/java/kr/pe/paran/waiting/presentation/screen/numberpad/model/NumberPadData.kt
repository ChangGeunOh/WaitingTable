package kr.pe.paran.waiting.presentation.screen.numberpad.model

import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import java.util.*

@kotlinx.serialization.Serializable
data class NumberPadData(
    val countWaiting: Int = 0,
    val receiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val transactionId: Long = Date().time
)
