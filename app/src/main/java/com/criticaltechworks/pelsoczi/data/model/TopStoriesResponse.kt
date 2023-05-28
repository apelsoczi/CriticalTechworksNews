package com.criticaltechworks.pelsoczi.data.model

import com.criticaltechworks.pelsoczi.data.serialization.AuthorSerializer
import com.criticaltechworks.pelsoczi.data.serialization.ContentSerializer
import com.criticaltechworks.pelsoczi.data.serialization.InstantISO8601Serializer
import com.criticaltechworks.pelsoczi.data.serialization.SerializableString
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Success and Error Response models for NewsApi.
 */
sealed interface TopStoriesResponse {

    /**
     * NewsApi success response.
     */
    @Serializable
    data class ApiResponse(
        // val status: String,
        val totalResults: Int,
        val articles: List<Article>
    ) : TopStoriesResponse {
        /**
         * Articles and Stories.
         */
        @Serializable
        data class Article(
            val source: Source,
            @Serializable(with = AuthorSerializer::class)
            val author: SerializableString = "",
            val title: String,
            val description: String = "",
            val url: String,
            val urlToImage: String = "",
            @Serializable(with = InstantISO8601Serializer::class)
            val publishedAt: Instant,
            @Serializable(with = ContentSerializer::class)
            val content: SerializableString = "",
        ) {
            /**
             * Meta-data describing the news source.
             */
            @Serializable
            data class Source(
                val id: String = "",
                val name: String,
            )
        }
    }

    /**
     * NewsApi error response.
     */
    @Serializable
    data class ApiError(
        // val status: String,
        val code: String,
        val message: String,
    ) : TopStoriesResponse

    /**
     * Network Connection is not available to the NewsApi.
     */
    object InternetConnectionFailure : TopStoriesResponse

}

