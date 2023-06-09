package com.criticaltechworks.pelsoczi.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.criticaltechworks.pelsoczi.data.model.Stories
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.ReadStory
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.RefreshStories
import com.criticaltechworks.pelsoczi.util.doNothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(StoriesViewState())
    val viewState: StateFlow<StoriesViewState> = _viewState.asStateFlow()

    /**
     * Single public accessor function to handle [StoriesViewIntent] submission to the viewModel.
     */
    fun handle(intent: StoriesViewIntent) = viewModelScope.launch(Dispatchers.Default) {
        when (intent) {
            is RefreshStories -> refreshStories()
            is ReadStory -> doNothing
        }
    }

    /**
     * Invoke the repository to fetch stories and either collect stories emissions or
     * handle the error invoking the repository.
     */
    private suspend fun refreshStories() {
        val loading = StoriesViewState(loading = true)
        _viewState.emit(loading)

        repository.stories().collect { stories ->
            when (stories) {
                is Stories.Headlines -> StoriesViewState(headlines = stories.headlines)
                is Stories.NoContent -> StoriesViewState(noContent = true)
                is Stories.Offline -> StoriesViewState(internetError = true)
            }.let {
                _viewState.emit(it)
            }
        }
    }

}