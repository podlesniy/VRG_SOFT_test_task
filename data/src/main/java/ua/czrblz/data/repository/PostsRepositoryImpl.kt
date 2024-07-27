package ua.czrblz.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ua.czrblz.data.paging.POSTS_LIMIT
import ua.czrblz.data.paging.PostsPagingSource
import ua.czrblz.domain.repository.PostsRepository

class PostsRepositoryImpl(
    private val postsPagingSource: PostsPagingSource
): PostsRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun getPostsForPaging() =
        Pager(PagingConfig(pageSize = POSTS_LIMIT)) {
            postsPagingSource
        }.flow.cachedIn(scope)

    override fun getTopPosts() = getPostsForPaging()
}