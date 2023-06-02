package com.criticaltechworks.pelsoczi.di

import android.content.Context
import coil.ImageLoader
import com.criticaltechworks.pelsoczi.data.remote.ImageDataSource
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import com.criticaltechworks.pelsoczi.util.kotlinxJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {


    @Provides
    fun provideCoilImageRequester(
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        networkDataSource: NetworkDataSource,
        imageDataSource: ImageDataSource,
    ): NewsRepository {
        return NewsRepository(
            json = kotlinxJson,
            networkDataSource = networkDataSource,
            coilDataSource = imageDataSource,
        )
    }

}