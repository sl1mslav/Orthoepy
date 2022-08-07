package com.example.orthoepy.entity

sealed class DictionaryFragmentPage {
    object Classic: DictionaryFragmentPage()
    object Exam: DictionaryFragmentPage()
    object Personal: DictionaryFragmentPage()
}
