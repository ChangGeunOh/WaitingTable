package kr.pe.paran.waiting.presentation.screen.manager

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.FoldAwareConfiguration
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.TwoPaneStrategy
import kr.pe.paran.waiting.presentation.screen.setting.userInteractionNotification

@Composable
fun ManagerContent(
    isDetailOpen: Boolean,
    detailKey: Any,
    paddingValues: PaddingValues,
    showListAndDetail: Boolean = true,
    twoPaneStrategy: TwoPaneStrategy,
    displayFeatures: List<DisplayFeature>,
    setIsDetailOpen: (Boolean) -> Unit,
    list: @Composable (listDetailVisible: Boolean) -> Unit,
    detail: @Composable (listDetailVisible: Boolean) -> Unit,
) {
    val currentIsDetailOpen by rememberUpdatedState(isDetailOpen)
    val currentShowListAndDetail by rememberUpdatedState(newValue = showListAndDetail)
    val currentDetailKey by rememberUpdatedState(detailKey)

    val showList by remember {
        derivedStateOf {
            currentShowListAndDetail || !currentIsDetailOpen
        }
    }
    val showDetail by remember {
        derivedStateOf {
            currentShowListAndDetail || currentIsDetailOpen
        }
    }
    // Validity check: we should always be showing something
    check(showList || showDetail)

    val listSavableStateHolder = rememberSaveableStateHolder()
    val detailSavableStateHolder = rememberSaveableStateHolder()

    val start = remember {
        movableContentOf {
            listSavableStateHolder.SaveableStateProvider(0) {
                Box(
                    modifier = Modifier
                        .userInteractionNotification {
                            setIsDetailOpen(false)
                        }
                ) {
                    list(showDetail)
                }
            }
        }
    }

    val end = remember {
        movableContentOf {
            detailSavableStateHolder.SaveableStateProvider(currentDetailKey) {
                Box(
                    modifier = Modifier
                        .userInteractionNotification {
                            setIsDetailOpen(true)
                        }
                ) {
                    detail(showList)
                }
            }

            if (!showList) {
                BackHandler {
                    setIsDetailOpen(false)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        if (showList && showDetail) {
            TwoPane(
                first = {
                    start()
                },
                second = {
                    end()
                },
                strategy = twoPaneStrategy,
                displayFeatures = displayFeatures,
                foldAwareConfiguration = FoldAwareConfiguration.VerticalFoldsOnly,
            )
        } else if (showList) {
            start()
        } else {
            end()
        }
    }

}