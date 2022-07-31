package com.example.orthoepy.data.repository

import com.example.orthoepy.data.database.DictionaryDao
import com.example.orthoepy.entity.Word
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    private val dictionaryDao: DictionaryDao
) {

    fun getAllWordsFromDB(): Flow<List<Word>> = dictionaryDao.getAll()

    suspend fun getWordById(id: Int): Flow<Word> = dictionaryDao.getWordById(id)

    fun getNotBoughtWords(): Flow<List<Word>> = dictionaryDao.getNotBoughtWords()

    fun getBoughtWords(): Flow<List<Word>> = dictionaryDao.getBoughtWords()

    suspend fun buyWordInDB(word: Word) = dictionaryDao.update(word.copy(isBought = "true"))

    suspend fun markWordAsFavouriteInDB(word: Word) = dictionaryDao.update(word.copy(isFavourite = "true"))

    suspend fun markWordAsNotFavouriteInDB(word: Word) = dictionaryDao.update(word.copy(isFavourite = "false"))
}