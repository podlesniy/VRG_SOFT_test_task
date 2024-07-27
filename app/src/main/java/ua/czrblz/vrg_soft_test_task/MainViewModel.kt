package ua.czrblz.vrg_soft_test_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.czrblz.domain.model.RedditChildren
import ua.czrblz.domain.usecase.GetTopPostsUseCase

class MainViewModel(
    private val getTopPostsUseCase: GetTopPostsUseCase,
): ViewModel() {

    private val _data = MutableStateFlow<PagingData<RedditChildren>?>(null)
    val data: StateFlow<PagingData<RedditChildren>?> = _data.asStateFlow()

    init {
        viewModelScope.launch {
            getTopPostsUseCase.execute().collect {
                _data.value = it
            }
        }
    }
}