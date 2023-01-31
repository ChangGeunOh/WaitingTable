package kr.pe.paran.waiting.presentation.screen.numberpad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.FoldAwareConfiguration
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.TwoPaneStrategy

@Composable
fun NumberPadContent(
    modifier: Modifier = Modifier,
    twoPaneStrategy: TwoPaneStrategy,
    displayFeatures: List<DisplayFeature>,
    showListAndDetail: Boolean,
    list: @Composable () -> Unit,
    detail: @Composable () -> Unit,
) {

    val currentShowListAndDetail by rememberUpdatedState(showListAndDetail)

    val start = remember {
        movableContentOf {
            Box(
            ) {
                list()
            }
        }
    }

    val end = remember {
        movableContentOf {
                Box{
                    detail()
                }
        }
    }

    Box(modifier = modifier) {
        if (currentShowListAndDetail) {
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
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            end()
        }
    }
}