package kr.pe.paran.waiting.presentation.screen.manager.contents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.presentation.component.NetworkStatusView
import kr.pe.paran.waiting.presentation.screen.manager.ManagerViewModel
import kr.pe.paran.waiting.ui.theme.Mint

@Composable
fun CallWaitingContent(
    viewModel: ManagerViewModel = hiltViewModel(),
) {

    val managerWaitingNumber by viewModel.managerWaitingNumber.collectAsState()

    val currentData = managerWaitingNumber.currentReceiptNumberData
    val nextData = managerWaitingNumber.nextReceiptNumberData

    val networkInfo by viewModel.networkInfo.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "대기 고객 : ${managerWaitingNumber.countWaiting}명",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row (modifier = Modifier
                .height(150.dp)
                .padding(horizontal = 32.dp)
            ){

                var buttonTitle = "호출 종료"
                var buttonSubTitle = ""
                var buttonEnabled = false
                if (currentData.waitingStatus in listOf(WaitingStatus.CALLING, WaitingStatus.CALLED)) {
                    buttonTitle = "${currentData.waitingNumber}번"
                    buttonSubTitle = "다시 호출하기"
                    buttonEnabled = true
                }
                var buttonBackgroundColor: Color

                WaitingManagerButton(
                    modifier = Modifier.weight(1f),
                    title = buttonTitle,
                    subTitle = buttonSubTitle,
                    background = Color.Gray,
                    enabled = buttonEnabled,
                    isRight = false,
                    onClick = viewModel::repeatNumber
                )

                Spacer(modifier = Modifier.width(8.dp))

                buttonTitle = "대기 없음"
                buttonSubTitle = "호출하기"
                buttonEnabled = true
                buttonBackgroundColor = Mint
                if (nextData.waitingStatus == WaitingStatus.WAIT) {
                    buttonTitle = "다음 ${nextData.waitingNumber}번"
                } else {
                    if (nextData.waitingStatus == WaitingStatus.NONE && currentData.waitingStatus in listOf(WaitingStatus.CALLED, WaitingStatus.CALLING)) {
                        buttonTitle = "호출종료"
                        buttonBackgroundColor = Color.Red
                    } else {
                        buttonEnabled = false
                    }
                    buttonSubTitle = ""
                }

                WaitingManagerButton(
                    modifier = Modifier.weight(1f),
                    title = buttonTitle,
                    subTitle = buttonSubTitle,
                    enabled = buttonEnabled,
                    background = buttonBackgroundColor,
                    isRight = true,
                    onClick = viewModel::nextNumber
                )
            }
        }
        NetworkStatusView(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(4.dp),
            networkInfo = networkInfo
        )
    }
}

@Composable
fun WaitingManagerButton(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    background: Color,
    enabled: Boolean = true,
    isRight: Boolean = true,
    onClick: () -> Unit
) {
    val shape = if (isRight) {
        RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
    } else {
        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
    }
    Button(
        modifier = modifier.fillMaxHeight(),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            if (subTitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}