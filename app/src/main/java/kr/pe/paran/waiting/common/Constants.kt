package kr.pe.paran.waiting.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import io.ktor.http.*
import kr.pe.paran.waiting.domain.model.MenuItem


object Constants {

    const val WAITING_MAX_NUMBER_DEFAULT = 999
    var isServerMode = true

    const val QR_CODE = "qrCode"

    const val SETTING_SECURITY_CODE = "0101232580"

    const val DATABASE_VERSION = 5
    const val DATABASE_NAME = "WaitingDatabase.db"

    val REMOTE_SERVER_PROTOCOL = URLProtocol.HTTP
    const val REMOTE_SERVER_HOST = "localhost"
    const val REMOTE_SERVER_PORT = 8080

    const val REMOTE_REPEAT_TIME = 1000L

    const val PUBLIC_IP_SERVER = "http://api.ipify.org/"

    val SETTING_MENUS = listOf<String>("모드 설정", "화면 설정", "App 정보")
    val MANAGER_MENUS = listOf(
        MenuItem(
            id = 0,
            title = "대기 호출",
            icon = Icons.Filled.Campaign
        ),
        MenuItem(
            id = 1,
            title = "대기 삭제",
            icon = Icons.Filled.Delete
        ),
    )


}
