package kr.pe.paran.waiting.presentation.screen.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.presentation.component.NetworkStatusView
import kr.pe.paran.waiting.ui.theme.Mint

@Composable
fun DisplayContent(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    displayStatus: DisplayStatus = DisplayStatus(),
    onClickSettings: () -> Unit = {},
    networkInfo: NetworkInfo
) {

    val displayReceiptNumberList = mutableListOf<ReceiptNumberData>().apply {
        addAll(Array(2 - displayStatus.doneReceiptNumberList.size) { ReceiptNumberData() }.toList())
        addAll(displayStatus.doneReceiptNumberList)
        add(displayStatus.callReceiptNumberData)
        addAll(displayStatus.waitReceiptNumberList)
        addAll(Array(2 - displayStatus.waitReceiptNumberList.size) { ReceiptNumberData() }.toList())
    }

    Row(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .weight(0.25f)
                .background(Color.Black)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            displayReceiptNumberList.forEach {
                DisplayItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    receiptNumberData = it
                )
            }
            BottomWaitingCount(modifier = Modifier, count = displayStatus.countWaiting)
        }

        Box(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxSize()
        ) {

            IconButton(
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.TopEnd),
                onClick = onClickSettings
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon",
                    tint = Color.LightGray
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayStatus.callReceiptNumberData.waitingNumber.toString(),
                    fontSize = 150.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "입장 해 주세요", fontSize = 32.sp, fontWeight = FontWeight.Medium)
            }

            NetworkStatusView(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                networkInfo = networkInfo
            )
        }
    }
}

@Composable
fun DisplayItem(
    modifier: Modifier = Modifier,
    receiptNumberData: ReceiptNumberData
) {

    val backgroundColor =
        if (receiptNumberData.waitingStatus == WaitingStatus.CALLING) Mint else Color.Transparent
    val textColor =
        if (receiptNumberData.waitingStatus == WaitingStatus.DONE) Color.Gray else Color.White
    Box(
        modifier = modifier
            .background(color = backgroundColor)
    ) {
        if (receiptNumberData.waitingStatus != WaitingStatus.NONE) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor)
                    .align(Alignment.Center),
                text = receiptNumberData.waitingNumber.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }

}


@Composable
fun BottomWaitingCount(modifier: Modifier, count: Int = 8) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "대기고객", fontSize = 18.sp, color = Mint, fontWeight = FontWeight.Bold)
        Text(
            text = "${count}명",
            fontSize = 36.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}