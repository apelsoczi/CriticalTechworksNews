package com.criticaltechworks.pelsoczi.ui.stories

import app.cash.turbine.test
import com.criticaltechworks.pelsoczi.data.model.Stories
import com.criticaltechworks.pelsoczi.data.model.Stories.NoContent
import com.criticaltechworks.pelsoczi.data.model.Stories.Offline
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.data.serialization.headlineOne
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
        coEvery { repository.stories() } returns flowOf(Offline)
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
        coEvery { repository.stories() } returns flowOf(NoContent)
        // when
        viewModel.viewState.test {
            viewModel.handle(RefreshStories)
            // then
            skipItems(1) // init state
            awaitItem().let {
                assertThat(it.loading)
            }
            awaitItem().let {
                assertThat(it.noContent)
            }
        }
        coVerify { repository.stories() }
    }

    @Test
    fun `stories returned from server`() = runTest {
        // given
        coEvery { repository.stories() } returns flowOf(Stories.Headlines(listOf(headlineOne)))
        // when
        viewModel.viewState.test {
            viewModel.handle(RefreshStories)
            // then
            skipItems(1) // init state
            awaitItem().let {
                assertThat(it.loading)
            }
            awaitItem().let {
                assertThat(it.headlines).isNotEmpty()
            }
        }
        coVerify { repository.stories() }
    }

}