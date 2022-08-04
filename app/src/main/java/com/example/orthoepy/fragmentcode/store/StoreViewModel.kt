package com.example.orthoepy.fragmentcode.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.entity.Word
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import com.example.orthoepy.entity.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    val notBoughtWords = repository.getNotBoughtWords().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    private val availableLetters = datastoreRepository.preferencesFlow.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _notBoughtWordsByQuery = MutableStateFlow<List<Word>?>(null)
    val notBoughtWordsByQuery = _notBoughtWordsByQuery.asStateFlow()

    private val wordsToBuy = MutableStateFlow<MutableList<Word>>(mutableListOf())

    val dataPackage = wordsToBuy.combine(availableLetters) { it1, it2 ->
        Pair(it1, it2)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Pair(wordsToBuy.value, availableLetters.value)
    )

    private fun increaseLetterCount(amount: Int) {
        viewModelScope.launch {
            datastoreRepository.increaseLetterCount(amount)
        }
    }

    fun addWordToBuy(word: Word): Boolean {
        return if (wordsToBuy.value.sumOf { it.wordText.length } + word.wordText.length <= availableLetters.value!!.currencyAmount) {
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

    fun clearCheckedWords() {
        viewModelScope.launch {
            wordsToBuy.value = mutableListOf()
        }
    }
    fun buyCheckedWords() {
        viewModelScope.launch {
            wordsToBuy.value.forEach {
                repository.buyWordInDB(it)
            }
            increaseLetterCount(-(wordsToBuy.value.sumOf { it.wordText.length }))
            wordsToBuy.value = mutableListOf()
        }
    }
}