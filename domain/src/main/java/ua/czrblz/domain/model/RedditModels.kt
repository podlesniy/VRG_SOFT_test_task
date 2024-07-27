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
    val title: String,
    val author: String,
    val subreddit: String,
    val url: String,
    val thumbnail: String,
    val name: String
)