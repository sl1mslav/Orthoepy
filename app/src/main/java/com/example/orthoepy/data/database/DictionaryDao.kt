package com.example.orthoepy.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT * FROM dictionary WHERE id = :id")
    fun getWordById(id: Int): Flow<Word>

    @Query("SELECT * FROM dictionary WHERE isBought = 'false' ORDER BY wordText ASC")
    fun getNotBoughtWords(): Flow<List<Word>>

    @Query("SELECT * FROM dictionary WHERE isBought = 'true' ORDER BY wordText ASC")
    fun getBoughtWords(): Flow<List<Word>>

    @Delete
    fun delete(word: Word)

}