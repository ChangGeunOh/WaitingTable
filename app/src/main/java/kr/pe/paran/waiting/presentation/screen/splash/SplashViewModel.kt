package kr.pe.paran.waiting.presentation.screen.splash

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.model.AppMode
import kr.pe.paran.waiting.domain.model.ScreenMode
import kr.pe.paran.waiting.domain.model.SettingData
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.ktor.model.ServerAction
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private var _settingData = MutableStateFlow<SettingData?>(null)
    val settingData = _settingData.asStateFlow()

    fun navNextScreen() {
        val screen = when(settingData.value!!.screenMode) {
            ScreenMode.NONE -> Screen.SettingScreen
            ScreenMode.WAITING -> Screen.AdvertisingScreen
            ScreenMode.MANAGER -> Screen.ManagerScreen
            ScreenMode.DISPLAY -> Screen.DisplayScreen
        }
        navigator.navigate(screen)
    }

    fun startSever(context: Context) {
        Logcat.i("startServer>${settingData.value?.appMode}")
        if (settingData.value?.appMode == AppMode.SERVER) {
            val intent = Intent().apply {
                action = ServerAction.SERVER_START
                component = ComponentName(context.packageName, "kr.pe.paran.waiting.receiver.MyReceiver")
                addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            }
            context.sendBroadcast(intent)
        }
    }

    init {
        viewModelScope.launch {
            _settingData.value = usesCases.settingDataUseCase()

        }
    }
}