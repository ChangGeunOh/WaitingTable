package kr.pe.paran.waiting.presentation.screen.numberpad

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import kotlinx.coroutines.*
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.getActivity
import kr.pe.paran.waiting.domain.model.NumberKeyData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.presentation.component.CustomDialog
import kr.pe.paran.waiting.ui.theme.Mint

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterial3WindowSizeClassApi
@Composable
fun NumberPadScreen(
    viewModel: NumberPadViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val windowSizeClass = calculateWindowSizeClass(activity = context.getActivity())
    val displayFeatures = calculateDisplayFeatures(activity = context.getActivity())
    val widthSizeClass by rememberUpdatedState(windowSizeClass.widthSizeClass)

//    val systemUiController = rememberSystemUiController()
//    DisposableEffect(key1 = systemUiController) {
//        systemUiController.isSystemBarsVisible = false
//        onDispose { }
//    }

    val receiptNumber by viewModel.receiptNumber.collectAsState()
    val message by viewModel.message.collectAsState()

    val waitingNumber by viewModel.waitingNumber.collectAsState()
    val numberPadData by viewModel.numberPadData.collectAsState()
    var isShowCallNumber by remember { mutableStateOf(false) }

    val networkInfo by viewModel.networkInfo.collectAsState()
    Logcat.i("NetworkInfo>${networkInfo.toString()}")

    LaunchedEffect(key1 = numberPadData.receiptNumberData.modifiedDate, block = {
        if (numberPadData.receiptNumberData.waitingStatus == WaitingStatus.CALLING) {
            viewModel.speechCalledNumber(receiptNumber = numberPadData.receiptNumberData)
            isShowCallNumber = true
        }
    })

    LaunchedEffect(key1 = message, block = {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    })

    val numberKeyRowList = (1..12).map {
        NumberKeyData(
            text = when (it) {
                10 -> "<"
                11 -> "0"
                12 -> "접수"
                else -> it.toString()
            },
            textColor = when (it) {
                10 -> Mint
                12 -> Color.White
                else -> Color.Gray
            },
            backgroundColor = if (it == 12) Mint else Color.Transparent
        )
    }.chunked(3)


    val showListAndDetail = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> true
        else -> true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(color = Mint),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "전화번호를 입력하세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = viewModel::onCloseScreen
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        NumberPadContent(
            modifier = Modifier,
            showListAndDetail = showListAndDetail,
            twoPaneStrategy = HorizontalTwoPaneStrategy(
                splitFraction = 0.35f,
            ),
            displayFeatures = displayFeatures,
            list = {
                NumberPadList(waitingCount = numberPadData.countWaiting, networkInfo = networkInfo)
            },
            detail = {
                NumberPadDetail(
                    numberKeyRowList = numberKeyRowList,
                    receiptNumber = receiptNumber,
                    onClickKey = {
                        viewModel.onClickKey(it)
                    }
                )
            }
        )
    }
    if (waitingNumber.isNotEmpty()) {
        WaitingNumberDialog(
            waitingNumber = waitingNumber,
            onClickDismiss = { viewModel.onDismissWaitingNumberDialog() }
        )
        LaunchedEffect(key1 = waitingNumber, block = {
            viewModel.textToSpeech(waitingNumber)
            coroutineScope.launch {
                delay(3000)
                viewModel.onDismissWaitingNumberDialog()
            }
        })
    }

    if (isShowCallNumber) {
        CallNumberDialog(callNumber = numberPadData.receiptNumberData.waitingNumber.toString())
        LaunchedEffect(key1 = Unit, block = {
            delay(2000)
            isShowCallNumber = false
        })
    }
}

@Composable
fun CallNumberDialog(
    callNumber: String = "",
) {
    CustomDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "대기번호", fontSize = 28.sp, fontWeight = FontWeight.Medium)
            Text(text = callNumber, fontSize = 86.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "입장 해 주세요.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


