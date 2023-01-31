package kr.pe.paran.waiting.presentation.screen.image

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.Utils
import kr.pe.paran.waiting.presentation.component.WaitingTopBar
import kr.pe.paran.waiting.ui.theme.Mint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    viewModel: ImageViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var imageSize by remember { mutableStateOf(Size(0, 0)) }

    val configuration = LocalConfiguration.current
    val ratio = configuration.screenWidthDp / configuration.screenHeightDp
    LaunchedEffect(key1 = ratio, block = {
        if (ratio > 0) {
            val width = (configuration.screenWidthDp - (4 * 3)) / 3
            val height = width / ratio
            imageSize = Size(width, height)
        }
    })

    val images by viewModel.imageList.collectAsState()

    val launcherPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted && (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                Utils.showDialogPermission(context = context)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }


    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents(),
            onResult = viewModel::onResult
        )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            WaitingTopBar(
                title = "대기 화면 사진",
                onClick = viewModel::popBackStack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Mint,
                contentColor = Color.White,
                onClick = { galleryLauncher.launch("image/*") }
            ) {
                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "Photo Add Icon")
            }
        }
    ) { paddingValues ->
        ImageContent(
            paddingValues = paddingValues,
            imageSize = imageSize,
            imageList = images,
            onClickRemove = viewModel::onRemove,
            onClickItem = viewModel::onClickItem
        )
    }
}