package com.criticaltechworks.pelsoczi.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
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
            RefreshStories -> doNothing
        }
    }

}