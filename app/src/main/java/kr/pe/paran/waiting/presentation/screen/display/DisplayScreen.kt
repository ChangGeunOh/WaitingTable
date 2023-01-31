package kr.pe.paran.waiting.presentation.screen.display

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.WaitingStatus

@ExperimentalMaterial3Api
@Composable
fun DisplayScreen(
    viewModel: DisplayViewModel = hiltViewModel()
) {

    val displayStatus by viewModel.displayStatus.collectAsState()
    val networkInfo by viewModel.networkInfo.collectAsState()

    LaunchedEffect(key1 = displayStatus.callReceiptNumberData.modifiedDate, block = {
        if (displayStatus.callReceiptNumberData.waitingStatus == WaitingStatus.CALLING) {
            viewModel.textToSpeech(displayStatus.callReceiptNumberData)
        }
    })

    Logcat.i(displayStatus.toString())
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        DisplayContent(
            paddingValues = paddingValues,
            displayStatus = displayStatus,
            onClickSettings = viewModel::navSettings,
            networkInfo = networkInfo
        )
    }

}