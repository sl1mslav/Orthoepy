package com.example.orthoepy.fragmentcode.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.repository.DatastoreRepository
import com.example.orthoepy.data.repository.DictionaryRepository
import com.example.orthoepy.entity.DictionaryFragmentPage
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
    private val datastoreRepository: DatastoreRepository
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

    val availableLetters = datastoreRepository.preferencesFlow.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
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
            val queryCorrected = query.trim().lowercase()
            _wordsByQuery.value = wordsNoQuery.filter { word ->
                queryCorrected.length <= word.wordText.length &&
                        queryCorrected in word.wordText.subSequence(0, queryCorrected.length)
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