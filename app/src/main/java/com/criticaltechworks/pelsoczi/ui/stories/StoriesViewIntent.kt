package com.criticaltechworks.pelsoczi.ui.stories

/**
 * Represents actions from the UI, either dispatched by the user or by the system (when an animation
 * completes, or a UI event listener is invoked as a callback)
 */
sealed interface StoriesViewIntent {
    object RefreshStories : StoriesViewIntent
}