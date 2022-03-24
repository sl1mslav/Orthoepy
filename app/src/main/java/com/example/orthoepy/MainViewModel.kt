package com.example.orthoepy

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.orthoepy.data.repository.DatastoreRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel (application: Application) : AndroidViewModel(application) {

    @Inject lateinit var datastoreRepository: DatastoreRepository

    init {
        Log.d("MainViewModel", "Букв: ")
        (application as App).getAppComponent().inject(this)
    }

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

    //val getLetterCount = datastoreRepository.getLetterCountFlow()

}