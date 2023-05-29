package com.criticaltechworks.pelsoczi.data.repository

import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiError
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.InternetConnectionFailure
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
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
     * @return the most recent news stories for the selected news source.
     */
    suspend fun fetchStories(): TopStoriesResponse {
        val response = networkDataSource.fetchTopStories()
        return when (response) {
            is Ok -> {
                if (response.isSuccess) json.decodeFromString<ApiResponse>(response.body)
                else json.decodeFromString<ApiError>(response.body)
            }
            else -> InternetConnectionFailure
        }
    }

}
