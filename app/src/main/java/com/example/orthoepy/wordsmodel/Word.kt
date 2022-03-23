package com.example.orthoepy.wordsmodel

data class Word(
    val id: Int,
    val wordText: String,
    val wordTextStress: String,
    val stressIndex: Int,
    var memorizationLevel: Int,
    var isBought: String,
    var isFavourite: String,
    var isChecked: Boolean = false
)
