package kr.pe.paran.waiting.presentation.screen.image.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val usesCases: UsesCases
): ViewModel() {

    val index: Int = checkNotNull(savedStateHandle["index"])

    private var _imageList = MutableStateFlow(emptyList<String>())
    val imageList = _imageList.asStateFlow()

    fun popBackStack() {
        navigator.popBackStack()
    }

    init {
        viewModelScope.launch {
            _imageList.value = usesCases.displayImagesUseCase()
        }
    }

}