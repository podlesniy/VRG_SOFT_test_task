package ua.czrblz.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ua.czrblz.data.api.ApiService
import ua.czrblz.domain.model.RedditChildren

const val POSTS_LIMIT = 10
class PostsPagingSource(
    private val apiService: ApiService,
): PagingSource<String, RedditChildren>() {

    override fun getRefreshKey(state: PagingState<String, RedditChildren>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildren> {
        return try {
            val response = apiService.getTopPosts(POSTS_LIMIT, params.key)
            val responseData = response.body()?.data?.children ?: emptyList()
            val nextKey: String = responseData.last().data.postName
            LoadResult.Page(data = responseData, null, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }
}