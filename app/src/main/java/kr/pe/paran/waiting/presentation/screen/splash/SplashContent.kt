package kr.pe.paran.waiting.presentation.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kr.pe.paran.waiting.presentation.component.TypewriterText

@Composable
fun SplashContent(
    paddingValues: PaddingValues,
    onFinishTypeWrite: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()
    ) {
        TypewriterText(
            modifier = Modifier.align(Alignment.Center),
            text = "Table Waiting...",
            onFinishTypeWrite = onFinishTypeWrite
        )
    }

}