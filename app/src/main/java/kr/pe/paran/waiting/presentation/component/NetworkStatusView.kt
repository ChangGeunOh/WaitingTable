package kr.pe.paran.waiting.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.NetworkType

@Composable
fun NetworkStatusView(modifier: Modifier, networkInfo: NetworkInfo) {

    val typeIcon = when (networkInfo.type) {
        NetworkType.NONE -> Icons.Outlined.WarningAmber
        NetworkType.WIFI -> Icons.Outlined.Wifi
        NetworkType.CELLULAR -> Icons.Outlined.SignalCellularAlt
        NetworkType.ETHERNET -> Icons.Outlined.Lan
    }
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = typeIcon,
            contentDescription = "Network Icon",
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = networkInfo.deviceAddress, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = networkInfo.serverAddress, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Outlined.Dns,
            contentDescription = "Server Icon",
            tint = if (networkInfo.isLiveServer) Color.Gray else Color.LightGray
        )
    }
}