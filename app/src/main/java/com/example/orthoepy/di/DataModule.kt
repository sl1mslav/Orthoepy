package com.example.orthoepy.di

import android.content.Context
import com.example.orthoepy.data.repository.DatastoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDatastoreRepository(context: Context) : DatastoreRepository {
        return DatastoreRepository(context = context)
    }

}