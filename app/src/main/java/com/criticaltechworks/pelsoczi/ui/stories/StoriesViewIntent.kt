package com.criticaltechworks.pelsoczi.ui.stories

import com.criticaltechworks.pelsoczi.data.model.Headline

/**
 * Represents actions from the UI, either dispatched by the user or by the system (when an animation
 * completes, or a UI event listener is invoked as a callback)
 */
sealed interface StoriesViewIntent {

    /** Refresh the stories from the endpoint */
    object RefreshStories : StoriesViewIntent

    /** View a stories content preview */
    data class ReadStory(
        val headline: Headline
    ) : StoriesViewIntent

}