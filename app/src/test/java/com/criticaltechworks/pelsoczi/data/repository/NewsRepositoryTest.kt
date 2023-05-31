package com.criticaltechworks.pelsoczi.data.repository

import app.cash.turbine.test
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Failure
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import com.criticaltechworks.pelsoczi.data.serialization.errorJson
import com.criticaltechworks.pelsoczi.data.serialization.successJson
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NewsRepositoryTest {

    private lateinit var newsRepository: NewsRepository

    private val dataSource = mockk<NetworkDataSource>()

    @Before
    fun setUp() {
        newsRepository = NewsRepository(
            kotlinxJson,
            dataSource
        )
    }

    @Test
    fun `no internet connection or unknown host http errors emits failure`() = runTest {
        // given
        coEvery { dataSource.fetchTopStories() } returns Failure(mockk())
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isNull()
            }
            awaitComplete()
        }
    }

    @Test
    fun `http ok and server response error, emits empty list of articles`() = runTest {
        // given
        val json = errorJson
        coEvery { dataSource.fetchTopStories() } returns Ok(401, json.toString())
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isEmpty()
            }
            awaitComplete()
        }
    }

    @Test
    fun `server response is valid, and articles are sorted in descending order`() = runTest {
        // given
        val json = successJson
        coEvery { dataSource.fetchTopStories() } returns Ok(200, json.toString())
        val emissions = mutableListOf<List<Article>?>()
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                println(it)
                assertThat(it).isNotEmpty()
                assertThat(it).isNotNull()
                it?.let { list ->
                    assertThat(list[0].publishedAt.isAfter(list[1].publishedAt))
                    assertThat(list[1].publishedAt.isAfter(list[2].publishedAt))
                    assertThat(list[2].publishedAt.isAfter(list[3].publishedAt))
                }
            }
            awaitComplete()
        }
    }

}