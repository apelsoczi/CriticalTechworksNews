package com.criticaltechworks.pelsoczi.data.model

import android.graphics.drawable.Drawable
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article

/**
 * Convenience class to wrap deserialized [Article] with downloaded image [Drawable],
 * and property access syntax for article source inner class field access.
 */
data class Headline(
    private val article: Article,
    val cachedImageKey: String?,
) {
    val source = article.source.name
    val author = article.author
    val title = article.title
    val description = article.description
    val url = article.url
    val published = article.publishedAt
    val content = article.content
}