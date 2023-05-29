package com.criticaltechworks.pelsoczi.di

import com.criticaltechworks.pelsoczi.BuildConfig
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)

        val apiKeyHeaderInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("x-api-key", BuildConfig.API_KEY)
            return@Interceptor chain.proceed(request.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(apiKeyHeaderInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideJson(): Json {
        return kotlinxJson
    }

}