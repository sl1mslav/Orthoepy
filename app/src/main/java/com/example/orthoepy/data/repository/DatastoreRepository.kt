package com.example.orthoepy.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.orthoepy.entity.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatastoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.datastore by preferencesDataStore(name = "datastore")

    private companion object {
        val letterCountKey = intPreferencesKey("LETTER_COUNT")
        const val DEFAULT_CURRENCY_AMOUNT = 100
    }

    suspend fun increaseLetterCount(letterCount: Int) {
        context.datastore.edit { counter ->
            val currentCounterValue = counter[letterCountKey] ?: DEFAULT_CURRENCY_AMOUNT
            counter[letterCountKey] = currentCounterValue + letterCount
        }
    }

    private suspend fun setLetterCount(letterCount: Int) {
        context.datastore.updateData { counter ->
            counter.toMutablePreferences().apply {
                this[letterCountKey] = letterCount
            }
        }
    }

    val preferencesFlow: Flow<UserPreferences> = context.datastore.data.map { preferences ->
        val currencyAmount = preferences[letterCountKey] ?: DEFAULT_CURRENCY_AMOUNT
        UserPreferences(currencyAmount)
    }
}