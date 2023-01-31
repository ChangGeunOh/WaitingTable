package kr.pe.paran.waiting.presentation.screen.manager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import kr.pe.paran.waiting.domain.model.MenuItem
import kr.pe.paran.waiting.domain.model.ReceiptNumberData

data class ManagerState (
    var menuList: List<MenuItem> = emptyList(),
    val currentReceiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val nextReceiptNumberData: ReceiptNumberData = ReceiptNumberData(),
    val countWaiting: Int = 0,
){
    init {
        menuList = listOf(
            MenuItem(
                id = 0,
                title = "대기 호출",
                icon = Icons.Filled.Campaign
            ),
        )
    }
}