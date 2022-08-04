package com.example.orthoepy.fragmentcode.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import com.example.orthoepy.entity.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: DictionaryRepository,
): ViewModel() {
    val boughtWords = repository.getBoughtWords().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _boughtWordsByQuery = MutableStateFlow<List<Word>?>(null)
    val boughtWordsByQuery = _boughtWordsByQuery.asStateFlow()

    fun selectWordsByQuery(query: String) {
        viewModelScope.launch {
            val queryTrimmed = query.trim()
            _boughtWordsByQuery.value = boughtWords.value.filter { word ->
                queryTrimmed.length <= word.wordText.length &&
                        queryTrimmed in word.wordText.subSequence(0, queryTrimmed.length)
            }
        }
    }

    fun markWord(word: Word) {
        viewModelScope.launch {
            if (word.isFavourite.toBooleanStrict()) {
                repository.unmarkWordAsFavouriteInDB(word)
            }
            else repository.markWordAsFavouriteInDB(word)
        }
    }
}