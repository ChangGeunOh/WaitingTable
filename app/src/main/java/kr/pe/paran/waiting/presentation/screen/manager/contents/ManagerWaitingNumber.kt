package kr.pe.paran.waiting.presentation.screen.manager.contents

import kr.pe.paran.waiting.domain.model.ReceiptNumberData

@kotlinx.serialization.Serializable
data class ManagerWaitingNumber(
    val currentReceiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val nextReceiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val countWaiting: Int = 0
)