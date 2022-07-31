package com.example.orthoepy.fragmentcode.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.entity.Word
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    //TODO: find a way to get rif of getLetterCount()

    val boughtWords = repository.getBoughtWords()

    val notBoughtWords = repository.getNotBoughtWords().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    private val _notBoughtWordsByQuery = MutableStateFlow<List<Word>?>(null)
    val notBoughtWordsByQuery = _notBoughtWordsByQuery

    private val wordsToBuy = MutableStateFlow<MutableList<Word>>(mutableListOf())

    private val availableLetters = MutableStateFlow<Int?>(null)

    val dataPackage = wordsToBuy.combine(availableLetters) { it1, it2 ->
        Pair(it1, it2)
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

    fun selectWordsByQuery(query: String) {
        viewModelScope.launch {
            val queryTrimmed = query.trim()
            _notBoughtWordsByQuery.value = notBoughtWords.value.filter { word ->
                queryTrimmed.length <= word.wordText.length &&
                        queryTrimmed in word.wordText.subSequence(0, queryTrimmed.length)
            }
        }
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