package ua.czrblz.domain.usecase

import ua.czrblz.domain.repository.PostsRepository

class GetTopPostsUseCase(
    private val postsRepository: PostsRepository
) {

    fun execute() = postsRepository.getTopPosts()
}