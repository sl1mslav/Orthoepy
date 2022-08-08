package com.example.orthoepy.fragmentcode.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.repository.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository
): ViewModel() {
    val availableLetters = datastoreRepository.preferencesFlow.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )
}