package com.criticaltechworks.pelsoczi.data.repository

import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiError
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Repository of news articles and stories from the NewsApi.
 */
class NewsRepository @Inject constructor(
    private val json: Json,
    private val networkDataSource: NetworkDataSource,
) {

    /**
     * Retrieve articles and stories from the NewsApi for the news source.
     *
     * @return the list of items returned by the server, or null when an exception occurs.
     */
    suspend fun stories(): Flow<List<Article>?> = flow {
        val response = networkDataSource.fetchTopStories()
        if (response is Ok) {
            if (response.isSuccess) {
                val data = json.decodeFromString<ApiResponse>(response.body)
                data.articles.sortedByDescending { it.publishedAt }.let {
                    emit(it)
                }
            } else {
                json.decodeFromString<ApiError>(response.body)
                emit(emptyList())
            }
        } else {
            emit(null)
        }
    }

}
