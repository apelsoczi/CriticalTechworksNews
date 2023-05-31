package com.criticaltechworks.pelsoczi.ui.stories

import app.cash.turbine.test
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.ui.stories.StoriesViewIntent.RefreshStories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StoriesViewModelTest {

    private lateinit var viewModel: StoriesViewModel

    private val repository = mockk<NewsRepository>()

    @Before
    fun setUp() {
        viewModel = StoriesViewModel(
            repository = repository
        )
    }

    @Test
    fun `stories could not be fetched without internet`() = runTest {
        // given
        coEvery { repository.stories() } returns flowOf(null)
        // when
        viewModel.viewState.test {
            // then
            viewModel.handle(RefreshStories)
            skipItems(1) // init state
            awaitItem().let {
                assertThat(it.loading).isTrue()
            }
            awaitItem().let {
                assertThat(it.internetError).isTrue()
            }
        }
        coVerify { repository.stories() }
    }

    @Test
    fun `server responded without content`() = runTest {
        // given
        coEvery { repository.stories() } returns flowOf(emptyList())
        // when
        viewModel.viewState.test {
            viewModel.handle(RefreshStories)
            // then
            skipItems(1) // init state
            awaitItem().let {
                assertThat(it.loading)
            }
            awaitItem().let {
                assertThat(it.contentError)
            }
        }
        coVerify { repository.stories() }
    }

    @Test
    fun `stories returned from server`() = runTest {
        // given
        coEvery { repository.stories() } returns flowOf(listOf(mockk()))
        // when
        viewModel.viewState.test {
            viewModel.handle(RefreshStories)
            // then
            skipItems(1) // init state
            awaitItem().let {
                assertThat(it.loading)
            }
            awaitItem().let {
                assertThat(it.articles).isNotEmpty()
            }
        }
        coVerify { repository.stories() }
    }

}