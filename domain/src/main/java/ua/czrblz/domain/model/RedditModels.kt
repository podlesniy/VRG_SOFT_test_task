package ua.czrblz.domain.model

data class RedditResponse(
    val data: RedditData
)

data class RedditData(
    val children: List<RedditChildren>
)

data class RedditChildren(
    val data: RedditPost
)

data class RedditPost(
    val subreddit: String,
    val num_comments: Int,
    val created: Long,
    val thumbnail: String,
    val name: String
)