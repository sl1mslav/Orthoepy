package com.example.orthoepy.fragmentcode.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.repository.DictionaryRepository
import com.example.orthoepy.entity.DictionaryFragmentPage
import com.example.orthoepy.entity.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: DictionaryRepository,
) : ViewModel() {

    val boughtWords = repository.getBoughtWords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val favouriteWords = repository.getFavouriteWords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val examWords = repository.getExamWords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _wordsByQuery = MutableStateFlow<List<Word>?>(null)
    val wordsByQuery = _wordsByQuery.asStateFlow()

    fun selectWordsByQuery(query: String, page: DictionaryFragmentPage) {
        viewModelScope.launch {
            val wordsNoQuery = when (page) {
                is DictionaryFragmentPage.Classic -> boughtWords.value
                is DictionaryFragmentPage.Exam -> examWords.value
                is DictionaryFragmentPage.Personal -> favouriteWords.value
            }
            val queryTrimmed = query.trim()
            _wordsByQuery.value = wordsNoQuery.filter { word ->
                queryTrimmed.length <= word.wordText.length &&
                        queryTrimmed in word.wordText.subSequence(0, queryTrimmed.length)
            }
        }
    }

    fun markWord(word: Word) {
        viewModelScope.launch {
            if (word.isFavourite.toBooleanStrict()) {
                repository.unmarkWordAsFavouriteInDB(word)
            } else repository.markWordAsFavouriteInDB(word)
        }
    }
}