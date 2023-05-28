package com.criticaltechworks.pelsoczi

import android.app.Application
import com.criticaltechworks.pelsoczi.data.remote.NetworkDataSource
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var networkDataSource: NetworkDataSource

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            networkDataSource.fetchTopStories()
        }
    }

}