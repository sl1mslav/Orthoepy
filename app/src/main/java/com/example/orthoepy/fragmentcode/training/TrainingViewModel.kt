package com.example.orthoepy.fragmentcode.training

import androidx.lifecycle.ViewModel
import com.example.orthoepy.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val repository: DictionaryRepository
): ViewModel() {

    fun getRandomAvailableWords() = repository.getRandomWordsByLimit()
}