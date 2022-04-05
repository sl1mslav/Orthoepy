package com.example.orthoepy.data.repository

import androidx.lifecycle.LiveData
import com.example.orthoepy.data.database.DictionaryDao
import com.example.orthoepy.data.database.Word
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    private val dictionaryDao: DictionaryDao
) {

    var allWords: Flow<List<Word>> = dictionaryDao.getAll()

    fun getWordById(id: Int): Flow<Word> = dictionaryDao.getWordById(id)

    var getNotBoughtWords = dictionaryDao.getNotBoughtWords()

    var getBoughtWords = dictionaryDao.getBoughtWords()

}