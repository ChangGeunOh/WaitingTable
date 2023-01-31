package kr.pe.paran.waiting.presentation.screen.manager.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.presentation.screen.manager.ManagerViewModel

@Composable
fun RemoveWaitingContent(viewModel: ManagerViewModel) {

    Box(modifier = Modifier.fillMaxSize()) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.Center),
                onClick = viewModel::clearReceiptNumber
                ,
                colors = ButtonDefaults
                    .buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Gray,
                    ),
                shape = RectangleShape,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    text = "대기(웨이팅) 번호 삭제",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
}

