package kr.pe.paran.waiting.presentation.screen.advertising

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.presentation.screen.numberpad.CallNumberDialog

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPagerApi
@Composable
fun AdvertisingScreen(
    viewModel: AdvertisingViewModel = hiltViewModel()
) {

    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = systemUiController) {
        systemUiController.isSystemBarsVisible = false
        onDispose {
//            systemUiController.isSystemBarsVisible = true
        }
    }


    val numberPadData by viewModel.numberPadData.collectAsState()

    var isShowCallNumber by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = numberPadData.receiptNumberData.modifiedDate, block = {
        if (numberPadData.receiptNumberData.waitingStatus == WaitingStatus.CALLING) {
            viewModel.speechCalledNumber(receiptNumber = numberPadData.receiptNumberData)
            isShowCallNumber = true
        }
    })


    val adImages by viewModel.adImages.collectAsState()
    val imageList = adImages.ifEmpty { listOf("img_ad_0.jpg", "img_ad_1.jpg", "img_ad_2.jpg") }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        AdvertisingContent(
            imageList = imageList,
            waitingCount = numberPadData.countWaiting,
            onClickSetting = viewModel::navImages,
            onClickWaiting = viewModel::navNumberPad
        )
    }

    if (isShowCallNumber) {
        CallNumberDialog(callNumber = numberPadData.receiptNumberData.waitingNumber.toString())
        LaunchedEffect(key1 = Unit, block = {
            delay(2000)
            isShowCallNumber = false
        })
    }
}
