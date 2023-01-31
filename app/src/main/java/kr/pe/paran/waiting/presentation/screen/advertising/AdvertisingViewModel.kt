package kr.pe.paran.waiting.presentation.screen.advertising

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.data.repository.Repository
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import kr.pe.paran.waiting.presentation.screen.numberpad.model.NumberPadData
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AdvertisingViewModel @Inject constructor(
    @ApplicationContext application: Context,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    val numberPadData = usesCases.numberPadUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NumberPadData()
    )

    private val _adImages = MutableStateFlow(emptyList<String>())
    val adImages = _adImages.asStateFlow()

    fun navNumberPad() {
        navigator.navigate(Screen.NumberPadScreen)
    }

    fun navImages() {
        navigator.navigate(Screen.ImageScreen)
    }

    private fun getDisplayImages() {
        viewModelScope.launch {
            _adImages.value = usesCases.displayImagesUseCase()
        }
    }

    fun speechCalledNumber(receiptNumber: ReceiptNumberData) {
        val speechText = "${receiptNumber.waitingNumber} 번 입장 해 주세요."
        textToSpeech.speak(speechText, TextToSpeech.QUEUE_ADD, null, null)
        receiptNumber.waitingStatus = WaitingStatus.CALLED
        viewModelScope.launch {
            usesCases.receiptNumberUseCase(receiptNumberData = receiptNumber)
        }
    }

    init {
        getDisplayImages()
        textToSpeech = TextToSpeech(application) {
            if (it == TextToSpeech.SUCCESS) {
                this.textToSpeech.language = Locale.KOREAN
            }
        }
    }
}