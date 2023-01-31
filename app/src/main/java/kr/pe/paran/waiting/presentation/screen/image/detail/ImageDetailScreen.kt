package kr.pe.paran.waiting.presentation.screen.image.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    viewModel: ImageDetailViewModel = hiltViewModel()
) {

    val index = viewModel.index
    val imageList by viewModel.imageList.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        ImageDetailContent(
            paddingValues = it,
            index = index,
            imageList = imageList,
            onClickBack = viewModel::popBackStack
        )
    }
}