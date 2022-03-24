package com.example.orthoepy.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class DatastoreRepository(private val context: Context) {

    private val Context.datastore by preferencesDataStore(name = "datastore")

    companion object {
        val letterCountKey = intPreferencesKey("LETTER_COUNT")
    }

    suspend fun increaseLetterCount(letterCount: Int) {
        context.datastore.edit { counter ->
            val currentCounterValue = counter[letterCountKey] ?: 0
            counter[letterCountKey] = currentCounterValue + letterCount
        }
    }

    suspend fun setLetterCount(letterCount: Int) {
        context.datastore.edit { counter ->
            counter[letterCountKey] = letterCount
        }
    }

    suspend fun getLetterCount(): Int {
        val preferences = context.datastore.data.first()
        return preferences[letterCountKey] ?: 111
    }


}