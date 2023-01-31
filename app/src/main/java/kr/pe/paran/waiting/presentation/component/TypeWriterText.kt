package kr.pe.paran.waiting.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    modifier: Modifier = Modifier,
    text: String = "",
    onFinishTypeWrite: () -> Unit
) {
    var textToDisplay by remember { mutableStateOf("") }

    LaunchedEffect(
        key1 = Unit,
    ) {
        text.forEach {
            textToDisplay += it
            delay(160)
        }
        delay(1000)
        onFinishTypeWrite()
    }

    Text(
        modifier = modifier,
        text = textToDisplay,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
}