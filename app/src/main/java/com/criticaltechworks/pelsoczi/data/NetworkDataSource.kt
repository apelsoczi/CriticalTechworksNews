package com.criticaltechworks.pelsoczi.data

import android.content.Context
import com.criticaltechworks.pelsoczi.BuildConfig
import com.criticaltechworks.pelsoczi.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class NetworkDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @ApplicationContext private val context: Context,
) {

    private val httpUrl: HttpUrl = "${BuildConfig.NEWS_API}/v2/top-headlines/"
        .toHttpUrl()
        .newBuilder()
        .addQueryParameter("sources", context.getString(R.string.API_SOURCE))
        .build()

    /**
     * Makes a request to the NewsApi top headlines endpoint, for the build variant news source flavor.
     * makes a request and transforms the response to a [NetworkResult]
     *
     * @param httpUrl specify the [HttpUrl] to use in `/test` execution, by default `null`
     */
    suspend fun fetchTopStories(httpUrl: HttpUrl? = null): Response {
        val topStoriesHttpUrl = when (httpUrl) {
            null -> this@NetworkDataSource.httpUrl
            else -> httpUrl
        }
        val response = executeRequest(topStoriesHttpUrl)
        return response
    }

    /**
     * Launches a coroutine with [Dispatchers.IO] to request from the network.
     *
     * @param url the configured endpoint url to fetch a response from
     *
     * @return the response result from the NewsApi.
     */
    private suspend fun executeRequest(url: HttpUrl): Response = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        val response = okHttpClient.newCall(request).execute()
        response
    }

}