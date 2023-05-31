package com.criticaltechworks.pelsoczi.ui.stories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.RefreshStories
import com.criticaltechworks.pelsoczi.util.RememberSaveableEffect


@Composable
fun StoriesScreen(
    viewModel: StoriesViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    RememberSaveableEffect {
        viewModel.handle(RefreshStories)
    }
}

