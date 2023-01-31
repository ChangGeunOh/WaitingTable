package kr.pe.paran.waiting.presentation.screen.setting

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
import kr.pe.paran.waiting.domain.model.MenuItem

@Composable
fun SettingContent(
    isDetailOpen: Boolean,
    detailKey: Any,
    paddingValues: PaddingValues,
    showListAndDetail: Boolean = true,
    twoPaneStrategy: TwoPaneStrategy,
    displayFeatures: List<DisplayFeature>,
    setIsDetailOpen: (Boolean) -> Unit,
    list: @Composable (listDetailVisible: Boolean) -> Unit,
    detail: @Composable (listDetailVisible: Boolean) -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {

    val currentIsDetailOpen by rememberUpdatedState(isDetailOpen)
    val currentShowListAndDetail by rememberUpdatedState(showListAndDetail)
    val currentDetailKey by rememberUpdatedState(detailKey)

    // Determine whether to show the list and/or the detail.
    // This is a function of current app state, and the width size class.
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
            // Set up a SaveableStateProvider so the list state will be preserved even while it
            // is hidden if the detail is showing instead.
            listSavableStateHolder.SaveableStateProvider(0) {
                Box(
                    modifier = Modifier
                        .userInteractionNotification {
                            // When interacting with the list, consider the detail to no longer be
                            // open in the case of resize.
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
            // Set up a SaveableStateProvider against the selected word index to save detail
            // state while switching between details.
            // If this behavior isn't desired, this can be replaced with a key on the
            // selectedWordIndex.
            detailSavableStateHolder.SaveableStateProvider(currentDetailKey) {
                Box(
                    modifier = Modifier
                        .userInteractionNotification {
                            // When interacting with the detail, consider the detail to be
                            // open in the case of resize.
                            setIsDetailOpen(true)
                        }
                ) {
                    detail(showList)
                }
            }

            // If showing just the detail, allow a back press to hide the detail to return to
            // the list.
            if (!showList) {
                BackHandler {
                    setIsDetailOpen(false)
                }
            }
        }
    }

    Box(modifier = modifier) {
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
                modifier = modifier
                    .padding(paddingValues)
                ,
            )
        } else if (showList) {
            start()
        } else {
            end()
        }
    }

}