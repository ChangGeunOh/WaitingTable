package kr.pe.paran.waiting.presentation.screen.setting.sub_contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.Utils
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.domain.model.IpAddress
import kr.pe.paran.waiting.presentation.screen.setting.SettingState
import kr.pe.paran.waiting.ui.theme.TitleColor

@ExperimentalComposeUiApi
@Composable
fun ModeTypeContent(
    settingState: SettingState,
    onClickQr: () -> Unit = {},
    onClickItem: (AppMode) -> Unit = {},
    onChangedText: (String) -> Unit = {},
    onChangePort: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 32.dp)
    ) {

        Logcat.i("ModeTypeContent>${settingState.toString()}")
        item {
            SettingModeItem(
                text = "서버 모드",
                isSelected = settingState.appMode == AppMode.SERVER,
                onClick = { onClickItem(AppMode.SERVER) }
            )
            if (settingState.appMode == AppMode.SERVER) {
                SettingServeMode(
                    port = settingState.port.toString(),
                    privateAddress = settingState.loadPrivateAddress(),
                    publicAddress = settingState.loadPublicAddress(),
                    onChangePort = onChangePort
                )
            }
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            SettingModeItem(
                text = "클라이언트 모드",
                isSelected = settingState.appMode == AppMode.CLIENT,
                onClick = { onClickItem(AppMode.CLIENT) }
            )
            if (settingState.appMode == AppMode.CLIENT) {
                SettingClientMode(
                    ipAddress = settingState.serverAddress.getIpAddress(),
                    onClickQr = onClickQr,
                    onChangeText = onChangedText
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingModeItem(
    text: String = "서버 모드",
    isSelected: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = TitleColor
        )
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(selected = isSelected, onClick = onClick)
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun SettingServeMode(
    privateAddress: String = "",
    publicAddress: String = "",
    port: String = "",
    onClickItem: () -> Unit = {},
    onChangePort: (String) -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x0A000000))
            .padding(horizontal = 16.dp)
            .clickable { onClickItem() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SettingServeModeItem(
                title = "사설 주소 (IP)",
                ipAddress = privateAddress
            )
            Spacer(modifier = Modifier.weight(1f))
            SettingServeModeItem(
                title = "공인 주소 (IP)",
                ipAddress = publicAddress
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text("포트 번호", fontSize = 14.sp, color = Color(0x9A000000))

        TextField(
            modifier = Modifier
                .width(150.dp),
            value = port,
            singleLine = true,
            onValueChange = onChangePort,
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SettingServeModeItem(
    title: String = "사설 주소 (IP)",
    ipAddress: String = ""
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 16.sp, color = TitleColor)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(Color.White)
        ) {
            if (ipAddress.isNotEmpty()) {
                Image(
                    modifier = Modifier.size(120.dp),
                    bitmap = Utils.convertQRCodeToBitmap(ipAddress).asImageBitmap(),
                    contentDescription = "QR Code"
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = ipAddress, fontSize = 16.sp, color = TitleColor)
    }
}


@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun SettingClientMode(
    ipAddress: String = "172.30.1.15",
    onChangeText: (String) -> Unit = {},
    onClickQr: () -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0x0A000000))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text("서버 주소", fontSize = 14.sp, color = Color(0x9A000000))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = ipAddress,
            singleLine = true,
            onValueChange = {
                onChangeText(it)
            },
            trailingIcon = {
                IconButton(
                    onClick = onClickQr
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "QR Code Scanner"
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            textStyle = TextStyle(fontSize = 16.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
    }
}