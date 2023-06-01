package com.criticaltechworks.pelsoczi.data.remote

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.criticaltechworks.pelsoczi.data.model.TopStoriesResponse.ApiResponse.Article
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A network data source which wraps around the Coil library to downlad article images to the disk
 * cache, and provide those images to the ui on demand.
 */
class ImageDataSource @Inject constructor(
    private val imageLoader: ImageLoader,
    @ApplicationContext private val context: Context,
) {

    /** Download images to disk */
    private val requestBuilder: ImageRequest.Builder by lazy {
        ImageRequest.Builder(context)
            .memoryCachePolicy(CachePolicy.DISABLED)
            .decoderFactory { sourceResult, options, imageLoader ->
                Decoder { DecodeResult(ColorDrawable(Color.TRANSPARENT), false) }
            }
    }

    /**
     * Bulk download images, by mapping an async task to each image download, and deletes disk cache
     * when a new bulk download is invoked.
     *
     * Executes allrequests to Coil [ImageLoader] simultaneously, improves performance time for 100
     * images from 18s (using `.enqueue`) to 6s.
     *
     * @return a list of articles and [SuccessResult] download disk-cache keys
     */
    suspend fun downloadImages(
        articles: List<Article>
    ): List<Pair<Article, String?>> = withContext(Dispatchers.IO) {
        deleteDiskCache()
        val tasks = articles
            .filter { it.urlToImage.isNotBlank() }
            .map { article ->
                async {
                    val request = requestBuilder.data(article.urlToImage).build()
                    val imageResult = imageLoader.execute(request)
                    article to imageResult
                }
            }
        return@withContext tasks.awaitAll()
            .filter { it.second is SuccessResult }
            .map { it.first to it.second as SuccessResult }
            .map { it.first to it.second.diskCacheKey }
    }

    /**
     * delete the local disk cache
     */
    @OptIn(ExperimentalCoilApi::class)
    fun deleteDiskCache() = imageLoader.diskCache?.clear()

}