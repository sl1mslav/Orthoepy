package com.example.orthoepy.fragmentcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.database.Word
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    val boughtWords = repository.getBoughtWords()

    val notBoughtWords = repository.getNotBoughtWords().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val wordsToBuy = MutableStateFlow<MutableList<Word>>(mutableListOf())

    private val availableLetters = MutableStateFlow<Int?>(null)

    val dataPackage = wordsToBuy.combine(availableLetters) {
        it1, it2 -> Pair(it1, it2)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Pair(wordsToBuy.value, availableLetters.value)
    )

    init {
        getLetterCount()
    }

    private fun increaseLetterCount(amount: Int) {
        viewModelScope.launch {
            datastoreRepository.increaseLetterCount(amount)
            getLetterCount()
        }
    }

    fun setLetterCount(amount: Int) {
        viewModelScope.launch {
            datastoreRepository.setLetterCount(amount)
            getLetterCount()
        }
    }

    private fun getLetterCount() {
        viewModelScope.launch {
            availableLetters.value = datastoreRepository.getLetterCount()
        }
    }

    fun addWordToBuy(word: Word): Boolean {
        return if (wordsToBuy.value.sumOf { it.wordText.length } + word.wordText.length <= availableLetters.value!!) {
            wordsToBuy.update { (it + word).toMutableList() }
            true
        } else false
    }

    fun removeWordToBuy(word: Word) {
        wordsToBuy.update { (it - word).toMutableList() }
    }

    fun buyCheckedWords() {
        viewModelScope.launch {
            wordsToBuy.value.forEach {
                repository.buyWordInDB(it)
            }
            increaseLetterCount(-(wordsToBuy.value.sumOf { it.wordText.length }))
            wordsToBuy.value = mutableListOf()
            getLetterCount()
        }
    }
}