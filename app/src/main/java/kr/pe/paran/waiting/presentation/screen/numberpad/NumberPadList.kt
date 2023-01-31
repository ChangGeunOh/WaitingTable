package kr.pe.paran.waiting.presentation.screen.numberpad

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.presentation.component.NetworkStatusView
import kr.pe.paran.waiting.ui.theme.BorderColor

@Composable
fun NumberPadList(
    waitingCount: Int,
    networkInfo: NetworkInfo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(5.dp))
                .padding(vertical = 16.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                    append("현재 대기")
                }
                withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Medium)) {
                    append("  $waitingCount")
                }
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                    append("팀")
                }
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(5.dp))
                .padding(all = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Sms,
                contentDescription = "sms icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "대기 등록시 메시지 알림을 보내드리며, 현재 대기 상황 및 예상 대기 시간을 확인할 수 있습니다.",
                fontSize = 13.sp
            )
        }

        NetworkStatusView(
            modifier = Modifier.fillMaxWidth(),
            networkInfo = networkInfo
        )
    }
}

