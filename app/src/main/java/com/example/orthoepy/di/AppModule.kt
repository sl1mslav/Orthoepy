package com.example.orthoepy.di

import android.app.Application
import android.content.Context
/*import com.example.orthoepy.MainViewModelFactory*/
import com.example.orthoepy.data.repository.DatastoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }
}

