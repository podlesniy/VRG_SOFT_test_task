package ua.czrblz.domain.repository

import ua.czrblz.domain.model.RedditResponse

interface PostsRepository {
    suspend fun getTopPosts(limit: Int, after: String?): RedditResponse
}