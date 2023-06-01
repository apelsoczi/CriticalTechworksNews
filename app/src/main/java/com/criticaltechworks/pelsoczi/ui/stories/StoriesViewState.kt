package com.criticaltechworks.pelsoczi.ui.stories

import com.criticaltechworks.pelsoczi.data.model.Headline

/**
 * Represents the UI state for [StoriesScreen]
 */
data class StoriesViewState(
    val loading: Boolean = false,
    val headlines: List<Headline> = emptyList(),
    val noContent: Boolean = false,
    val internetError: Boolean = false,
)