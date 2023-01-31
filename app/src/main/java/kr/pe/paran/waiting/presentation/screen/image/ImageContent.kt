package kr.pe.paran.waiting.presentation.screen.image

import android.net.Uri
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kr.pe.paran.waiting.common.utils.Logcat

@Composable
fun ImageContent(
    paddingValues: PaddingValues,
    imageSize: Size,
    imageList: List<String>,
    onClickRemove: (Int) -> Unit,
    onClickItem: (Int) -> Unit
) {

    LazyVerticalGrid(
        modifier = Modifier.padding(2.dp),
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(items = imageList) { index, item ->
            Box(
                modifier = Modifier
                    .clickable(onClick = { onClickItem(index) })
                    .fillMaxWidth()
                    .height(imageSize.height.dp),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(model = Uri.parse(item)),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    modifier = Modifier.align(alignment = Alignment.BottomEnd),
                    onClick = { onClickRemove(index) }
                ) {
                    Icon(
                        imageVector = Icons.Default.HighlightOff,
                        contentDescription = "Remove Icon",
                        tint = Color.Gray
                    )
                }

            }
        }
    }
}
