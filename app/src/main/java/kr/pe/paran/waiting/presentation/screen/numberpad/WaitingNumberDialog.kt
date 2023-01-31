package kr.pe.paran.waiting.presentation.screen.numberpad

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.presentation.component.CustomDialog
import kr.pe.paran.waiting.ui.theme.Mint

@Preview(showBackground = true)
@Composable
fun WaitingNumberDialog(
    waitingNumber: String = "101",
    onClickDismiss: () -> Unit = {}
) {
    CustomDialog(onDismissRequest = onClickDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "대기번호", fontSize = 28.sp, fontWeight = FontWeight.Medium)
            Text(text = waitingNumber, fontSize = 86.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onClickDismiss,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults
                    .buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Mint,
                    ),
                shape = RectangleShape,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "닫기",
                    fontSize = 24.sp
                )
            }
        }
    }
}