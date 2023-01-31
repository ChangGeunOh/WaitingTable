package kr.pe.paran.waiting.presentation.screen.setting

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.common.utils.NetworkUtils
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.domain.model.MenuItem
import kr.pe.paran.waiting.domain.model.ScreenMode
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.ktor.model.ServerAction
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.NavigatorData
import kr.pe.paran.waiting.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private var _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()


    init {
        getSettingState(context = context)
//        context.applicationInfo.targetSdkVersion
//        context.applicationInfo.minSdkVersion
    }

    private fun getSettingState(context: Context) {
        viewModelScope.launch {
            val settingData = usesCases.settingDataUseCase()
            _settingState.value = settingState.value.copy(
                privateAddress = NetworkUtils.getDeviceAddress(),
                serverAddress = settingData.serverAddress,
                screenMode = settingData.screenMode,
                appMode = settingData.appMode,
                port = settingData.port.toString()
            )
            _settingState.value = settingState.value.copy(menuList = getMenuList(context))
            _settingState.value = settingState.value.copy(
                publicAddress = NetworkUtils.getPublicAddress()
            )
        }
    }

    private fun getMenuList(context: Context): List<MenuItem> {

        val packageManager = context.packageManager

        @Suppress("DEPRECATION")            // Android 13 (TIRAMISU) Deprecated
        val appVersion = packageManager.getPackageInfo(context.packageName, 0).versionName


        return Constants.SETTING_MENUS.mapIndexed { index, value ->
            MenuItem(
                id = index,
                title = value,
                description = when (index) {
                    0 -> if (settingState.value.appMode == AppMode.SERVER) "서버 모드" else "클라이언트 모드"
                    1 -> settingState.value.screenMode.korean
                    2 -> "Version $appVersion"
                    else -> ""
                }
            )
        }
    }

    fun setServerAddress(ipAddress: String) {
        val host = ipAddress.split(":").first()
        val port = ipAddress.split(":").last().toIntOrNull() ?: 2842
        _settingState.value = settingState.value.copy(
            serverAddress = settingState.value.serverAddress.copy(host = host, port = port)
        )
        saveSettingData()

    }

    fun setPort(port: String) {
        _settingState.value = settingState.value.copy(port = port)
        saveSettingData()
    }

    private fun saveSettingData() {
        viewModelScope.launch {
            usesCases.settingDataUseCase(_settingState.value.getSettingData())
            Logcat.i(_settingState.value.getSettingData().toString())
//            usesCases.serverIpUseCase(ipAddress = ipAddress)
        }
    }


    fun setModeType(context: Context, type: AppMode) {
        _settingState.value = settingState.value.copy(
            appMode = type
        )
        saveSettingData()
    }


    fun startStopServer(context: Context, isStart: Boolean) {
        val intent = Intent().apply {
            action =
                if (isStart) ServerAction.SERVER_START else ServerAction.SERVER_STOP
            component =
                ComponentName(context.packageName, "kr.pe.paran.waiting.receiver.MyReceiver")
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        }
        context.sendBroadcast(intent)
    }

    fun setScreenType(context: Context, screenMode: ScreenMode) {
        _settingState.value = settingState.value.copy(
            screenMode = screenMode
        )
        _settingState.value = settingState.value.copy(menuList = getMenuList(context))
        saveSettingData()
    }

    fun popBackStack() {
        viewModelScope.launch {
            val screen = when (settingState.value.screenMode) {
                ScreenMode.WAITING -> Screen.NumberPadScreen
                ScreenMode.MANAGER -> Screen.ManagerScreen
                ScreenMode.DISPLAY -> Screen.DisplayScreen
                else -> Screen.SettingScreen
            }
            val navigatorData = NavigatorData(
                route = screen.route,
                popUpTo = "/",
                inclusive = false
            )
            Logcat.i(navigatorData.toString())
            navigator.navigate(navigatorData)
        }
    }

}