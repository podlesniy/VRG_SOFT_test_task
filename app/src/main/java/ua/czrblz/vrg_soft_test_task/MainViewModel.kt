package ua.czrblz.vrg_soft_test_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.czrblz.domain.model.RedditChildren
import ua.czrblz.domain.usecase.GetTopPostsUseCase
import ua.czrblz.domain.usecase.SavePictureInGalleryUseCase

class MainViewModel(
    private val getTopPostsUseCase: GetTopPostsUseCase,
    private val savePictureInGalleryUseCase: SavePictureInGalleryUseCase
): ViewModel() {

    private val _data = MutableStateFlow<PagingData<RedditChildren>?>(null)
    val data: StateFlow<PagingData<RedditChildren>?> = _data.asStateFlow()

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    init {
        viewModelScope.launch {
            getTopPostsUseCase.execute().collect {
                _data.value = it
            }
        }
    }

    fun savePicture(imageUrl: String, onComplete: (String) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            savePictureInGalleryUseCase.execute(imageUrl, onComplete, onError)
        }
    }

    fun updateImageUrl(imageUrl: String) {
        _currentImageUrl.value = imageUrl
    }
}