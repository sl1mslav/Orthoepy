package com.example.orthoepy.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DatastoreRepository @Inject constructor(private val context: Context) {

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
        context.datastore.updateData { counter ->
            counter.toMutablePreferences().apply {
                this[letterCountKey] = letterCount
            }
        }
    }

    suspend fun getLetterCount(): Int {
        val preferences = context.datastore.data.first()
        if (preferences[letterCountKey] == null) {
            setLetterCount(111)
        }
        return preferences[letterCountKey] ?: 111
    }
}