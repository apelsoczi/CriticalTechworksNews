package com.criticaltechworks.pelsoczi.data.remote

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.ErrorResult
import coil.request.SuccessResult
import com.criticaltechworks.pelsoczi.data.serialization.firstArticle
import com.criticaltechworks.pelsoczi.data.serialization.nullsArticle
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ImageDataSourceTest {

    lateinit var imageDataSource: ImageDataSource

    private val cache = mockk<DiskCache>() {
        coEvery { clear() } returns Unit
    }
    private val imageLoader = mockk<ImageLoader>() {
        coEvery { diskCache } returns cache
    }

    @Before
    fun setUp() {
        imageDataSource = ImageDataSource(
            imageLoader = imageLoader,
            context = mockk<Context>(),
        )
    }

    @Test
    fun `disk cache is deleted before bulk downloading`() = runTest {
        // given
        // when
        imageDataSource.downloadImages(emptyList())
        // then
        coVerify { cache.clear() }
    }

    @Test
    fun `articles without images are not downloaded`() = runTest {
        // given
        val articles = listOf(nullsArticle)
        // when
        val nullsResult = imageDataSource.downloadImages(articles)
        // then
        assertThat(nullsResult).isEmpty()
        coVerify { cache.clear() }
        // and when
        val emptyResult = imageDataSource.downloadImages(emptyList())
        // and then
        assertThat(emptyResult).isEmpty()
        coVerify { cache.clear() }
    }

    @Test
    fun `error requests while downloading are not returned`() = runTest {
        // given
        val articles = listOf(firstArticle)
        coEvery { imageLoader.execute(any()) } returns mockk<ErrorResult>()
        // when
        val result = imageDataSource.downloadImages(articles)
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `images are downloaded for articles and cached on disk`() = runTest {
        // given
        val articles = listOf(firstArticle)
        val successResult = mockk<SuccessResult>() {
            coEvery { this@mockk.diskCacheKey } returns firstArticle.urlToImage
        }
        coEvery { imageLoader.execute(any()) } returns successResult
        // when
        val result = imageDataSource.downloadImages(articles)
        // then
        assertThat(result).isNotEmpty()
        with(result) {
            assertThat(first().first).isEqualTo(firstArticle)
            assertThat(first().second).isEqualTo(successResult.diskCacheKey)
        }
    }

}