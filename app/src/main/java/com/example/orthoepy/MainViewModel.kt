package com.example.orthoepy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.database.Word
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var datastoreRepository: DatastoreRepository
    @Inject lateinit var dictionaryRepository: DictionaryRepository

    var getAllWords: LiveData<List<Word>>
    var getBoughtWords: LiveData<List<Word>>
    var getNotBoughtWords: LiveData<List<Word>>

    init {
        (application as App).getAppComponent().inject(this)
        getAllWords = getAll()
        getBoughtWords = getBoughtWords()
        getNotBoughtWords = getNotBoughtWords()
    }

    //DataStore requests
    fun increaseLetterCount(letters: Int) {
        viewModelScope.launch() {
            datastoreRepository.increaseLetterCount(letters)
        }
    }
    fun setLetterCount(letters: Int) {
        viewModelScope.launch() {
            datastoreRepository.setLetterCount(letters)
        }
    }
    fun getLetterCount(): Int = runBlocking {
        datastoreRepository.getLetterCount()
    }

    //Room requests
    fun getWordById(id: Int) = dictionaryRepository.getWordById(id).asLiveData()
    private fun getAll() = dictionaryRepository.allWords.asLiveData()
    private fun getBoughtWords() = dictionaryRepository.getBoughtWords.asLiveData()
    private fun getNotBoughtWords() = dictionaryRepository.getNotBoughtWords.asLiveData()
}