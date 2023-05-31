package com.criticaltechworks.pelsoczi

import android.app.Application
import com.criticaltechworks.pelsoczi.data.repository.NewsRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var repository: NewsRepository

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            repository.fetchStories()
        }
    }

}