package ua.czrblz.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ua.czrblz.domain.model.RedditChildren

interface PostsRepository {

    fun getTopPosts(): Flow<PagingData<RedditChildren>>
}