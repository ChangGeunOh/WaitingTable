package kr.pe.paran.waiting.presentation.screen.display

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.utils.NetworkUtils.flowNetworkInfo
import kr.pe.paran.waiting.data.remote.Network
import kr.pe.paran.waiting.data.remote.NetworkStatus
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    @ApplicationContext application: Context,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private var textToSpeech: TextToSpeech? = null

    val networkInfo = application.flowNetworkInfo().combine(Network.networkStatus) { networkInfo, networkStatus ->
        networkInfo.serverAddress = Network.serverAddress.value
        networkInfo.isLiveServer = networkStatus is NetworkStatus.Success
        networkInfo
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetworkInfo()
    )

    init {
        textToSpeech = TextToSpeech(application) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.KOREAN
            }
        }
    }

    var displayStatus = usesCases.displayWaitingUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DisplayStatus()
    )

    fun textToSpeech(receiptNumber: ReceiptNumberData) {
        val speechText = "${receiptNumber.waitingNumber} 번 입장 해 주세요."
        textToSpeech?.speak(speechText, TextToSpeech.QUEUE_ADD, null, null)
        receiptNumber.waitingStatus = WaitingStatus.CALLED
        viewModelScope.launch {
            usesCases.receiptNumberUseCase(receiptNumberData = receiptNumber)
        }

    }

    override fun onCleared() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onCleared()
    }

    fun navSettings() {
        navigator.navigate(Screen.SettingScreen)
    }

}