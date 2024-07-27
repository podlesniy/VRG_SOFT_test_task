package ua.czrblz.data.repository

import ua.czrblz.domain.model.RedditResponse
import ua.czrblz.domain.repository.PostsRepository

class PostsRepositoryImpl: PostsRepository {

    override suspend fun getTopPosts(limit: Int, after: String?): RedditResponse {
        TODO("Not yet implemented")
    }
}