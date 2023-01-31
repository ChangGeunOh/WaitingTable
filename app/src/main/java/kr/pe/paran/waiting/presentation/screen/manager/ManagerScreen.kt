package kr.pe.paran.waiting.presentation.screen.manager

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.getActivity
import kr.pe.paran.waiting.ui.theme.Mint

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen(
    viewModel: ManagerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val windowSizeClass = calculateWindowSizeClass(activity = context.getActivity())
    val displayFeatures = calculateDisplayFeatures(activity = context.getActivity())
    val widthSizeClass by rememberUpdatedState(windowSizeClass.widthSizeClass)

    val showListAndDetail = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> true
        else -> true
    }

    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }

    var isDetailOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = Mint),
                title = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "대기 관리 화면",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Mint),
                actions = {
                    IconButton(
                        onClick = viewModel::navSettingScreen
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Setting Icon",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ManagerContent(
            paddingValues = paddingValues,
            isDetailOpen = isDetailOpen,
            setIsDetailOpen = { isDetailOpen = it },
            detailKey = selectedIndex,
            showListAndDetail = showListAndDetail,
            twoPaneStrategy = HorizontalTwoPaneStrategy(
                splitFraction = 0.3f,
            ),
            displayFeatures = displayFeatures,
            list = {
                ManagerList(
                    menuList = Constants.MANAGER_MENUS,
                    onClickMenuItem = {
                        selectedIndex = it.id
                        isDetailOpen = true
                    },
                    indexSelected = selectedIndex
                )
            },
            detail = {
                ManagerDetail(
                    indexSelected = selectedIndex,
                    viewModel = viewModel
                )
            }
        )
    }

}