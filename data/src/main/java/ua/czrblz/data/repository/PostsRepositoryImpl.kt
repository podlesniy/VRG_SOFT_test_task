package ua.czrblz.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ua.czrblz.data.api.ApiService
import ua.czrblz.data.paging.POSTS_LIMIT
import ua.czrblz.data.paging.PostsPagingSource
import ua.czrblz.domain.repository.PostsRepository

class PostsRepositoryImpl(
    private val apiService: ApiService
): PostsRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    
    override fun getTopPosts() = Pager(PagingConfig(pageSize = POSTS_LIMIT)) {
        PostsPagingSource(apiService)
    }.flow.cachedIn(scope)
}