package kr.pe.paran.waiting.presentation.screen.numberpad

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.Constants
import kr.pe.paran.waiting.common.utils.NetworkUtils.flowNetworkInfo
import kr.pe.paran.waiting.data.remote.Network
import kr.pe.paran.waiting.data.remote.NetworkStatus
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NumberPadViewModel @Inject constructor(
    @ApplicationContext application: Context,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    private var _receiptNumber = MutableStateFlow<String>("010-")
    val receiptNumber = _receiptNumber.asStateFlow()

    private var _message = MutableStateFlow<String>("")
    val message = _message.asStateFlow()

    private var _waitingNumber = MutableStateFlow("")
    val waitingNumber = _waitingNumber.asStateFlow()

    val numberPadData = usesCases.numberPadUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NumberPadData()
    )

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
                this.textToSpeech.language = Locale.KOREAN
            }
        }
    }

    fun onClickKey(key: String) {
        val removedPhoneNumber = _receiptNumber.value.replace("-", "")
        when (key) {
            "접수" -> {
                processReceipt(phoneNumber = _receiptNumber.value.replace("-", ""))
            }
            "<" -> {
                if (removedPhoneNumber.length > 3) {
                    _receiptNumber.value = getPhoneNumberFormat(removedPhoneNumber.dropLast(1))
                }
            }
            else -> {
                if (removedPhoneNumber.length < 11) {
                    _receiptNumber.value =
                        getPhoneNumberFormat(removedPhoneNumber.plus(key))
                }
            }
        }
    }

    private fun processReceipt(phoneNumber: String) {
        if (Constants.SETTING_SECURITY_CODE == phoneNumber) {
            _message.value = "설정화면으로 이동합니다."
            navigator.navigate(Screen.SettingScreen)
        } else if (phoneNumber.length == 11) {
            saveReceiptNumber(number = phoneNumber)
            clearReceiptNumber()
        } else {
            _message.value = "휴대폰 번호를 입력하세요."
        }
    }

    private fun clearReceiptNumber() {
        _receiptNumber.value = "010-"
    }

    private fun saveReceiptNumber(number: String) {
        val receiptNumberData = ReceiptNumberData(
            phoneNumber = number,
            waitingStatus = WaitingStatus.WAIT
        )
        viewModelScope.launch {
            val waitingNumber =
                usesCases.receiptNumberUseCase(receiptNumberData = receiptNumberData)
            if (waitingNumber.toInt() > 0) {
                _waitingNumber.value = waitingNumber
            }
        }
    }

    private fun getPhoneNumberFormat(phone: String): String {
        val temp = phone.replace("-", "")
        var number = ""
        temp.forEachIndexed { index, char ->
            number += char
            if (index == 2 || index == 6) {
                number += "-"
            }
        }
        return number
    }

    fun onDismissWaitingNumberDialog() {
        _waitingNumber.value = ""
    }

    fun textToSpeech(number: String) {
        textToSpeech.speak("대기번호는 $number 입니다.", TextToSpeech.QUEUE_ADD, null, null)
    }

    override fun onCleared() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onCleared()
    }

    fun onCloseScreen() {
        navigator.popBackStack()
    }

    fun speechCalledNumber(receiptNumber: ReceiptNumberData) {
        val speechText = "${receiptNumber.waitingNumber} 번 입장 해 주세요."
        textToSpeech.speak(speechText, TextToSpeech.QUEUE_ADD, null, null)
        receiptNumber.waitingStatus = WaitingStatus.CALLED
        viewModelScope.launch {
            usesCases.receiptNumberUseCase(receiptNumberData = receiptNumber)
        }
    }
}