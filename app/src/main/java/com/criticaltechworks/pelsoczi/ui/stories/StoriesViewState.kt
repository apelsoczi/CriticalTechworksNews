package com.criticaltechworks.pelsoczi.ui.stories

import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article

/**
 * Represents the UI state for [StoriesScreen]
 */
data class StoriesViewState(
    val loading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val contentError: Boolean = false,
    val internetError: Boolean = false,
)