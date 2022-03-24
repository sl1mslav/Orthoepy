package com.example.orthoepy

import android.app.Application
import com.example.orthoepy.di.AppComponent
import com.example.orthoepy.di.AppModule
import com.example.orthoepy.di.DaggerAppComponent
import com.example.orthoepy.wordsmodel.WordsStoreService

class App: Application() {

    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    fun getAppComponent(): AppComponent = appComponent

    val wordsStoreService = WordsStoreService()
}