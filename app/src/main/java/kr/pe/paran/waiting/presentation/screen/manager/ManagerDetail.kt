package kr.pe.paran.waiting.presentation.screen.manager

import androidx.compose.runtime.Composable
import kr.pe.paran.waiting.presentation.screen.manager.contents.CallWaitingContent
import kr.pe.paran.waiting.presentation.screen.manager.contents.RemoveWaitingContent

@Composable
fun ManagerDetail(
    indexSelected: Int,
    viewModel: ManagerViewModel,
) {

    when(indexSelected) {
        0 -> {
            CallWaitingContent(
                viewModel = viewModel
            )
        }
        1 -> {
            RemoveWaitingContent(
                viewModel = viewModel
            )
        }
    }
}