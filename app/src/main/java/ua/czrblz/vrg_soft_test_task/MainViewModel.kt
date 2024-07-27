package ua.czrblz.vrg_soft_test_task

import androidx.lifecycle.ViewModel
import ua.czrblz.domain.usecase.GetTopPostsUseCase

class MainViewModel(
    private val getTopPostsUseCase: GetTopPostsUseCase,
): ViewModel() {

    fun getPosts() = getTopPostsUseCase.execute()
}