@file:OptIn(ExperimentalPagerApi::class)

package kr.pe.paran.waiting.presentation.screen.image.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageDetailContent(
    paddingValues: PaddingValues,
    index: Int,
    imageList: List<String> = emptyList(),
    onClickBack: () -> Unit
) {

    val pagerState = rememberPagerState(initialPage = index)

    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()
    ) {


        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            count = imageList.size
        ) { index: Int ->
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(model = Uri.parse(imageList[index])),
                contentDescription = "Image",
                contentScale = ContentScale.Crop
            )
        }

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            pagerState = pagerState,
            activeColor = Color(0xffffffff),
            inactiveColor = Color(0x80ffffff)
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onClick = onClickBack
        ) {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Back Icon")
        }
    }

}