package ua.czrblz.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ua.czrblz.data.paging.POSTS_LIMIT
import ua.czrblz.data.paging.PostsPagingSource
import ua.czrblz.domain.repository.PostsRepository

class PostsRepositoryImpl(
    private val postsPagingSource: PostsPagingSource
): PostsRepository {

    private fun getPostsForPaging() = Pager(PagingConfig(pageSize = POSTS_LIMIT)) {
        postsPagingSource
    }.flow

    override fun getTopPosts() = getPostsForPaging()
}