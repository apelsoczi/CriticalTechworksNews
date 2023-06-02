package com.criticaltechworks.pelsoczi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.DetailDestination.ARG_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val headline: Headline? by lazy {
        newsRepository.headline(
            headlineUrl = savedStateHandle.get<String>(ARG_URL) ?: ""
        )
    }

    private val _viewState = MutableStateFlow(DetailViewState(headline = headline))
    val viewState: StateFlow<DetailViewState> = _viewState.asStateFlow()
}