package kr.pe.paran.waiting.presentation.screen.image

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.navigation.Navigator
import kr.pe.paran.waiting.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val usesCases: UsesCases,
    private val navigator: Navigator
) : ViewModel() {

    private var _index = mutableStateOf(0)
    val index = _index.value

    private var _imageList = MutableStateFlow(emptyList<String>())
    val imageList = _imageList.asStateFlow()

    fun onResult(list: List<Uri>) {
        val imageList = list.map { it.toString() }
        _imageList.value = imageList
        saveImageList()
    }

    fun onClickItem(index: Int) {
        _index.value = index
        navigator.navigate(Screen.ImageDetailScreen.passIndex(index))
    }

    fun onRemove(index: Int) {
        val images = _imageList.value.toMutableList()
        images.removeAt(index)
        _imageList.value = images
        saveImageList()
    }

    private fun saveImageList() {
        viewModelScope.launch {
            usesCases.displayImagesUseCase(_imageList.value)
        }
    }

    fun popBackStack() {
        navigator.popBackStack()
    }

    init {
        viewModelScope.launch {
            _imageList.value = usesCases.displayImagesUseCase()
            Logcat.i(_imageList.value.toString())
        }
    }
}