package ua.czrblz.domain.model

import com.google.gson.annotations.SerializedName

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
    @SerializedName("subreddit") val authorName: String,
    @SerializedName("num_comments") val countComments: Int,
    @SerializedName("created") val createdTimestamp: Long,
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("url_overridden_by_dest") val imageUrl: String,
    @SerializedName("name") val postName: String
)