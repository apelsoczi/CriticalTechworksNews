package com.criticaltechworks.pelsoczi.ui.stories

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.data.serialization.headlineOne
import com.criticaltechworks.pelsoczi.ui.detail.DetailViewModel
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.DetailDestination.ARG_URL
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailViewModelTest {

    lateinit var viewModel: DetailViewModel

    private val newsRepository = mockk<NewsRepository>()

    @Test
    fun`load repository data by saved state handle navigation args` () = runTest {
        // given
        val headline = headlineOne
        coEvery { newsRepository.headline(any()) } returns headline
        // when
        viewModel = DetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf(ARG_URL to headline.url)),
            newsRepository = newsRepository,
        )
        viewModel.viewState.test {
            // then
            awaitItem().let {
                assertThat(it.headline).isEqualTo(headlineOne)
            }
            coVerify { newsRepository.headline(headline.url) }
        }
    }

}