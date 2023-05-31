package com.criticaltechworks.pelsoczi.data.remote

import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Failure
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException


class NetworkDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var networkDataSource: NetworkDataSource

    private val okHttpClient = OkHttpClient.Builder().build()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        networkDataSource = NetworkDataSource(
            okHttpClient,
            mockk(relaxed = true)
        )
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `200 request executed successfully`() = runTest {
        // given
        val response = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("/")
        )
        // then
        assertThat(result).isInstanceOf(Ok::class.java)
        result as Ok
        assertThat(result.isSuccess).isTrue()
        assertThat(result.code).isEqualTo(200)
    }

    @Test
    fun `400 request unacceptable, missing or misconfigured parameter`() = runTest {
        // given
        val response = MockResponse().setResponseCode(400)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("/")
        )
        // then
        assertThat(result).isInstanceOf(Ok::class.java)
        result as Ok
        assertThat(result.isSuccess).isFalse()
        assertThat(result.code).isEqualTo(400)
    }

    @Test
    fun `401 api key is missing or wasn't correct`() = runTest {
        // given
        val response = MockResponse().setResponseCode(401)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("/")
        )
        // then
        assertThat(result).isInstanceOf(Ok::class.java)
        result as Ok
        assertThat(result.isSuccess).isFalse()
        assertThat(result.code).isEqualTo(401)
    }

    @Test
    fun `429 rate limited, too many requests made within window`() = runTest {
        // given
        val response = MockResponse().setResponseCode(429)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("/")
        )
        // then
        assertThat(result).isInstanceOf(Ok::class.java)
        result as Ok
        assertThat(result.isSuccess).isFalse()
        assertThat(result.code).isEqualTo(429)
    }

    @Test
    fun `500 server error`() = runTest {
        // given
        val response = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("/")
        )
        // then
        assertThat(result).isInstanceOf(Ok::class.java)
        result as Ok
        assertThat(result.isSuccess).isFalse()
    }

    @Test
    fun `unknown host exception when there is no internet available`() = runTest{
        // given
        val response = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(response)
        // when
        val result = networkDataSource.fetchTopStories(
            httpUrl = mockWebServer.url("https://unknownhost")
        )
        // then
        assertThat(result).isInstanceOf(Failure::class.java)
        result as Failure
        assertThat(result.ex).isInstanceOf(IOException::class.java)
    }


}