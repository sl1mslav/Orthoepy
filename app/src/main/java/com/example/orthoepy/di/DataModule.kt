package com.example.orthoepy.di

import android.content.Context
import com.example.orthoepy.data.database.DictionaryDao
import com.example.orthoepy.data.database.DictionaryDatabase
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
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

    @Provides
    @Singleton
    fun provideDictionaryRepository(dictionaryDatabase: DictionaryDatabase) : DictionaryRepository {
        return DictionaryRepository(dictionaryDao = dictionaryDatabase.dictionaryDao())
    }

    @Provides
    @Singleton
    fun provideDictionaryDatabase(context: Context): DictionaryDatabase {
        return DictionaryDatabase.getDatabase(context = context)
    }

    @Provides
    @Singleton
    fun provideDictionaryDAO(dictionaryDatabase: DictionaryDatabase): DictionaryDao {
        return dictionaryDatabase.dictionaryDao()
    }

}