package kr.pe.paran.waiting.presentation.screen.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val settingData by viewModel.settingData.collectAsState()
    settingData?.let {
        viewModel.startSever(context = context)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        SplashContent(
            paddingValues = paddingValues,
            onFinishTypeWrite = viewModel::navNextScreen
        )
    }
}