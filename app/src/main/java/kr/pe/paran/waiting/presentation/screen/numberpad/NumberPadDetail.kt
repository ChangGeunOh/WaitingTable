package kr.pe.paran.waiting.presentation.screen.numberpad

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.NumberKeyData
import kr.pe.paran.waiting.ui.theme.BorderColor


@Composable
fun NumberPadDetail(
    numberKeyRowList: List<List<NumberKeyData>> = emptyList(),
    onClickKey: (String) -> Unit,
    receiptNumber: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val strokeWidth = 1 * density
                drawLine(
                    color = BorderColor,
                    start = Offset(0f, strokeWidth),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            }
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = receiptNumber,
            fontWeight = FontWeight.Medium,
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        )

        numberKeyRowList.forEach {
            Divider(modifier = Modifier.fillMaxWidth())
            NumberRowKey(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                numberKeyList = it,
                onClick = onClickKey
            )
        }

    }

}

@Composable
fun NumberRowKey(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    numberKeyList: List<NumberKeyData> = emptyList()
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        numberKeyList.forEachIndexed { index, numberKeyData ->
            NumberKey(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                value = numberKeyData.text,
                onClick = {
                    onClick(numberKeyData.text)
                },
                textColor = numberKeyData.textColor,
                backgroundColor = numberKeyData.backgroundColor
            )
            if (index != numberKeyList.lastIndex) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        }
    }
}

@Composable
fun NumberKey(
    modifier: Modifier = Modifier,
    value: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: (String) -> Unit = {}
) {
    TextButton(
        modifier = modifier
            .fillMaxHeight(),
        shape = RectangleShape,
        colors = ButtonDefaults.textButtonColors(backgroundColor = backgroundColor),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            Logcat.i("Key>$value")
            onClick(value)
        }
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = textColor
        )
    }
}