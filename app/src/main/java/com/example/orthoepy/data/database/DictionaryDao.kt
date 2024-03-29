package com.example.orthoepy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.example.orthoepy.entity.Word
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

    @Query("SELECT * FROM dictionary WHERE isFavourite = 'true' ORDER BY wordText ASC")
    fun getFavouriteWords() : Flow<List<Word>>

    @Query("SELECT * FROM dictionary WHERE isExamWord = 'true' ORDER BY wordText ASC")
    fun getExamWords() : Flow<List<Word>>

    @Query("SELECT * FROM dictionary WHERE isBought = 'true' OR isChecked = 'true' ORDER BY RANDOM() LIMIT :limit")
    fun getRandomWordsByLimit(limit: Int = 20) : Flow<List<Word>>

    @Update
    suspend fun update(word: Word)

    @Delete
    fun delete(word: Word)

}