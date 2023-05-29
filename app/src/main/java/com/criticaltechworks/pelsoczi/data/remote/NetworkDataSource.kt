package com.criticaltechworks.pelsoczi.data.remote

import android.content.Context
import com.criticaltechworks.pelsoczi.BuildConfig
import com.criticaltechworks.pelsoczi.R
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Failure
import com.criticaltechworks.pelsoczi.data.model.NetworkResponse.Ok
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject


/**
 * Represents the endpoints available for the NewsApi endpoint. Performs request creation, handling
 * responses and exceptions scenarios, and deserialization.
 */
class NetworkDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @ApplicationContext private val context: Context,
) {

    /* default endpoint to request in production */
    private val httpUrl: HttpUrl = "${BuildConfig.NEWS_API}/v2/top-headlines/"
//    private val httpUrl: HttpUrl = "https://newsapi.org/v2/everything?q=bitcoin&apiKey=88cbbeb0e4704701a2475133aea1eaef"
        .toHttpUrl()
        .newBuilder()
        .addQueryParameter("sources", context.getString(R.string.API_SOURCE))
        .build()

    /**
     * Makes a request to the NewsApi top headlines endpoint, for the build variant news source flavor.
     * makes a request and transforms the response to a [NetworkResponse]
     *
     * @param httpUrl specify the [HttpUrl] to use in `/test` execution, by default `null`
     */
    suspend fun fetchTopStories(httpUrl: HttpUrl? = null): NetworkResponse {
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
    private suspend fun executeRequest(url: HttpUrl): NetworkResponse = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            Ok(
                code = response.code,
                body = response.body?.string() ?: ""
            )
        } catch (ex: IOException) {
            Failure(ex)
        }
    }

}