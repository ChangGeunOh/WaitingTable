package kr.pe.paran.waiting.presentation.screen.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kr.pe.paran.waiting.common.utils.getActivity
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.presentation.component.WaitingTopBar

@ExperimentalComposeUiApi
@ExperimentalMaterial3WindowSizeClassApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = systemUiController)
    {
        systemUiController.isSystemBarsVisible = true
        onDispose {
//            systemUiController.isSystemBarsVisible = false
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.startStopServer(context = context, isStart = false)
    })

    val settingState by viewModel.settingState.collectAsState()

    val windowSizeClass = calculateWindowSizeClass(activity = context.getActivity())
    val displayFeatures = calculateDisplayFeatures(activity = context.getActivity())
    val widthSizeClass by rememberUpdatedState(windowSizeClass.widthSizeClass)

    val showListAndDetail = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> true
        else -> true
    }

    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var isDetailOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            WaitingTopBar(title = "설정") {
                if (settingState.appMode == AppMode.SERVER) {
                    viewModel.startStopServer(context = context, isStart = true)
                }
                viewModel.popBackStack()
            }
        }
    ) { paddingValues ->
        SettingContent(
            paddingValues = paddingValues,
            isDetailOpen = isDetailOpen,
            setIsDetailOpen = { isDetailOpen = it },
            detailKey = selectedIndex,
            showListAndDetail = showListAndDetail,
            twoPaneStrategy = HorizontalTwoPaneStrategy(
                splitFraction = 0.35f,
            ),
            displayFeatures = displayFeatures,
            list = {
                SettingList(
                    menuList = settingState.menuList,
                    onClickMenuItem = {
                        selectedIndex = it.id
                    },
                    indexSelected = selectedIndex
                )
            },
            detail = {
                SettingDetail(
                    indexSelected = selectedIndex,
                    viewModel = viewModel
                )
            }
        )
    }
}

