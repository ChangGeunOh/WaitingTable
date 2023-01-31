package kr.pe.paran.waiting.presentation.screen.advertising

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.*
import kr.pe.paran.waiting.ui.theme.Mint

//@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@ExperimentalPagerApi
@Composable
fun AdvertisingContent(
    waitingCount: Int = 0,
    onClickWaiting: () -> Unit,
    onClickSetting: () -> Unit,
    imageList: List<String>,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(
        key1 = pagerState.currentPage,
        block = {
            delay(3000)
            val newPosition = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(newPosition)
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            count = imageList.size
        ) { index: Int ->

            if ( imageList[index].startsWith("content") ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(model = Uri.parse(imageList[index])),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop
                )
            } else {
                val bitmap = context.assets.open(imageList[index]).use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = bitmap,
                    contentDescription = "Ad Image",
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onClickSetting
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Photo Icon",
                    tint = Color(0x50ffffff)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalPagerIndicator(
                modifier = Modifier,
                pagerState = pagerState,
                activeColor = Color(0xffffffff),
                inactiveColor = Color(0x80ffffff)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x8F000000)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 42.dp),
                    text = "현재 대기 ${waitingCount}팀",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    modifier = Modifier
                        .background(Mint)
                        .padding(
                            horizontal = 48.dp,
                            vertical = 24.dp
                        ),
                    onClick = onClickWaiting
                ) {
                    Text(
                        text = "대기접수",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }

            }
        }

    }

}