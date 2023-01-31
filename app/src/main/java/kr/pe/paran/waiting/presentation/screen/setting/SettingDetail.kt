package kr.pe.paran.waiting.presentation.screen.setting

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kr.pe.paran.waiting.data.remote.Network
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.presentation.screen.setting.sub_contents.AppInfoContent
import kr.pe.paran.waiting.presentation.screen.setting.sub_contents.ModeTypeContent
import kr.pe.paran.waiting.presentation.screen.setting.sub_contents.ScreenTypeContent

@ExperimentalComposeUiApi
@Composable
fun SettingDetail(
    indexSelected: Int,
    viewModel: SettingViewModel
) {

    val context = LocalContext.current
    val settingState by viewModel.settingState.collectAsState()

    Network.serverAddress.value = if (settingState.appMode == AppMode.CLIENT) settingState.serverAddress.getIpAddress() else ""

    val barcodeLauncher =
        rememberLauncherForActivityResult(contract = ScanContract()) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    context,
                    "Scanned: " + result.contents,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.setServerAddress(result.contents)
            }
        }

    when (indexSelected) {
        1 -> {
            ScreenTypeContent(
                selectedIndex = settingState.screenMode,
                onClick = { index -> viewModel.setScreenType(context = context, index)}
            )
        }
        0 -> {
            ModeTypeContent(
                settingState = settingState,
                onClickQr = {
                    val options = ScanOptions()
                    options.setOrientationLocked(false)
                    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    options.setPrompt("Scan a QR Code")
                    options.setCameraId(0)              // Use a specific camera of the device
                    options.setBeepEnabled(false)
                    options.setBarcodeImageEnabled(true)
                    barcodeLauncher.launch(options)
                },
                onClickItem = {
                    viewModel.setModeType(context = context, type =  it)
                },
                onChangedText = viewModel::setServerAddress,
                onChangePort = viewModel::setPort
            )
        }
        2 -> {
            AppInfoContent(storeVersion = "")
        }
    }
}