package com.example.orthoepy.di

import android.content.Context
import androidx.room.Room
import com.example.orthoepy.data.database.DictionaryDao
import com.example.orthoepy.data.database.DictionaryDatabase
import com.example.orthoepy.data.repository.DatastoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): DictionaryDatabase {
        return Room.databaseBuilder(
            appContext,
            DictionaryDatabase::class.java,
            "Dictionary"
        ).createFromAsset("database/dictionary.db").build()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context) = DatastoreRepository(appContext)

    @Provides
    fun provideContext(@ApplicationContext appContext: Context) = appContext

    @Provides
    fun provideDictionaryDao(dictionaryDatabase: DictionaryDatabase): DictionaryDao = dictionaryDatabase.dictionaryDao()
}

