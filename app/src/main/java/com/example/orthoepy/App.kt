package com.example.orthoepy

import android.app.Application
import com.example.orthoepy.wordsmodel.WordsStoreService

class App: Application() {
    val wordsStoreService = WordsStoreService()
}