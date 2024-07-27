package ua.czrblz.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.czrblz.domain.model.RedditResponse

interface ApiService {

    @GET("/top.json")
    suspend fun getTopPosts(
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): Response<RedditResponse>
}