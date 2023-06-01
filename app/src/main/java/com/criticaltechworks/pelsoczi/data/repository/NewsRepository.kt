package com.criticaltechworks.pelsoczi.data.repository

import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.Stories
import com.criticaltechworks.pelsoczi.data.model.Stories.Headlines
import com.criticaltechworks.pelsoczi.data.model.Stories.NoContent
import com.criticaltechworks.pelsoczi.data.model.Stories.Offline
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiError
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse
import com.criticaltechworks.pelsoczi.data.remote.ImageDataSource
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
    private val coilDataSource: ImageDataSource,
) {

    /**
     * Retrieve articles and stories from the NewsApi for the news source.
     *
     * @return the [Headlines] returned by the server, [NoContent], or [Offline].
     */
    suspend fun stories(): Flow<Stories> = flow {
        val response = networkDataSource.fetchTopStories()
        if (response is Ok) {
            if (response.isSuccess) {
                val data = json.decodeFromString<ApiResponse>(response.body)
                if (data.articles.isEmpty()) {
                    emit(NoContent)
                    return@flow
                }
                val images = coilDataSource.downloadImages(data.articles)
                if (images.isEmpty()) {
                    emit(NoContent)
                    return@flow
                }
                val headlines = images.map { Headline(it.first, it.second) }
                    .sortedByDescending { it.published }
                emit(Headlines(headlines))
            } else {
                json.decodeFromString<ApiError>(response.body)
                emit(NoContent)
            }
        } else {
            emit(Offline)
        }
    }

}
