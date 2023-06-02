package com.criticaltechworks.pelsoczi.data.repository

import app.cash.turbine.test
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Failure
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.Stories.Headlines
import com.criticaltechworks.pelsoczi.data.model.Stories.NoContent
import com.criticaltechworks.pelsoczi.data.model.Stories.Offline
import com.criticaltechworks.pelsoczi.data.remote.ImageDataSource
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import com.criticaltechworks.pelsoczi.data.serialization.articlesList
import com.criticaltechworks.pelsoczi.data.serialization.errorJson
import com.criticaltechworks.pelsoczi.data.serialization.successJson
import com.criticaltechworks.pelsoczi.data.serialization.successNoArticlesJson
import com.criticaltechworks.pelsoczi.data.serialization.successNullsJson
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NewsRepositoryTest {

    private lateinit var newsRepository: NewsRepository

    private val newsDataSource = mockk<NetworkDataSource>()
    private val coilDataSource = mockk<ImageDataSource>()

    @Before
    fun setUp() {
        newsRepository = NewsRepository(
            kotlinxJson,
            newsDataSource,
            coilDataSource,
        )
    }

    @Test
    fun `no internet connection or unknown host http errors emits offline`() = runTest {
        // given
        coEvery { newsDataSource.fetchTopStories() } returns Failure(mockk())
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isInstanceOf(Offline::class.java)
            }
            awaitComplete()
        }
    }

    @Test
    fun `http ok and server response error, emits no content`() = runTest {
        // given
        val json = errorJson
        coEvery { newsDataSource.fetchTopStories() } returns Ok(401, json.toString())
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isInstanceOf(NoContent::class.java)
            }
            awaitComplete()
        }
        coVerify { newsDataSource.fetchTopStories(any()) }
    }

    @Test
    fun `server response is valid, and no article json body emits no content`() = runTest {
        // given
        val json = successNoArticlesJson
        coEvery { newsDataSource.fetchTopStories() } returns Ok(200, json.toString())
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isInstanceOf(NoContent::class.java)
            }
            awaitComplete()
        }
        coVerify { newsDataSource.fetchTopStories(any()) }
    }

    @Test
    fun `server response is valid, and image downloads fail emits empty content`() = runTest {
        // given
        val json = successNullsJson
        coEvery { newsDataSource.fetchTopStories() } returns Ok(200, json.toString())
        coEvery { coilDataSource.downloadImages(any()) } returns emptyList()
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isInstanceOf(NoContent::class.java)
            }
            awaitComplete()
        }
        coVerify { newsDataSource.fetchTopStories(any()) }
        coVerify { coilDataSource.downloadImages(any()) }
    }

    @Test
    fun `server response valid, sort articles desc, headlines cache, and emit headlines`() = runTest {
        // given
        val json = successJson
        coEvery { newsDataSource.fetchTopStories() } returns Ok(200, json.toString())
        val articles = articlesList
        val downloads = buildList {
            add(articles[0] to articles[0].urlToImage)
            add(articles[1] to articles[1].urlToImage)
            add(articles[2] to articles[2].urlToImage)
            add(articles[3] to articles[3].urlToImage)
        }
        coEvery { coilDataSource.downloadImages(any()) } returns downloads
        // when
        newsRepository.stories().test {
            // then
            awaitItem().let {
                assertThat(it).isInstanceOf(Headlines::class.java)
                it as Headlines
                assertThat(it.headlines).isNotEmpty()
                assertThat(it.headlines[0].published.isAfter(it.headlines[1].published))
                assertThat(it.headlines[1].published.isAfter(it.headlines[2].published))
                assertThat(it.headlines[2].published.isAfter(it.headlines[3].published))
                assertThat(newsRepository.getHeadlinesCache()).hasSize(4)
                assertThat(newsRepository.headline(it.headlines[0].url)).isNotNull()
            }
            awaitComplete()
        }
        coVerify { newsDataSource.fetchTopStories(any()) }
        coVerify { coilDataSource.downloadImages(any()) }
    }

    @Test
    fun `subsequent calls to load article data clears the cache`() = runTest {
        // given
        `server response valid, sort articles desc, headlines cache, and emit headlines`()
        coEvery { newsDataSource.fetchTopStories() } returns Failure(mockk())
        // when
        newsRepository.stories().test {
            // then
            skipItems(1)
            awaitComplete()
            assertThat(newsRepository.getHeadlinesCache()).isEmpty()
        }
    }

}