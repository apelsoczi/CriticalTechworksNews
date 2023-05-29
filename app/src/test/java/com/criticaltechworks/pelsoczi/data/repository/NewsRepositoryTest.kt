package com.criticaltechworks.pelsoczi.data.repository

import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Failure
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiError
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.InternetConnectionFailure
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import com.criticaltechworks.pelsoczi.data.serialization.errorJson
import com.criticaltechworks.pelsoczi.data.serialization.successJson
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `no internet connection or unknown host http errors emits failure`() = runBlocking {
        // given
        coEvery { dataSource.fetchTopStories() } returns Failure(mockk())
        // when
        val result = newsRepository.fetchStories()
        // then
        assertThat(result).isInstanceOf(InternetConnectionFailure::class.java)
    }

    @Test
    fun `return api error when http completes and server response is error`() = runBlocking {
        // given
        val json = errorJson
        coEvery { dataSource.fetchTopStories() } returns Ok(401, json.toString())
        // when
        val result = newsRepository.fetchStories()
        // then
        assertThat(result).isInstanceOf(ApiError::class.java)
    }

    @Test
    fun `return api response when http completes and server response is valid`() = runBlocking {
        // given
        val json = successJson
        coEvery { dataSource.fetchTopStories() } returns Ok(200, json.toString())
        // when
        val result = newsRepository.fetchStories()
        // then
        assertThat(result).isInstanceOf(ApiResponse::class.java)
    }

}