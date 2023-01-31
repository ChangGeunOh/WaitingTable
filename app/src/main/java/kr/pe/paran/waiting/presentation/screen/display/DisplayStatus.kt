package kr.pe.paran.waiting.presentation.screen.display

import kr.pe.paran.waiting.domain.model.ReceiptNumberData

@kotlinx.serialization.Serializable
data class DisplayStatus(
    val doneReceiptNumberList: List<ReceiptNumberData> = emptyList(),
    val waitReceiptNumberList: List<ReceiptNumberData> = emptyList(),
    val callReceiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val countWaiting: Int = 0
)
