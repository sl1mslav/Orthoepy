package com.example.orthoepy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val wordText: String,
    val wordTextStress: String,
    val stressIndex: Int,
    val memorizationLevel: Int,

    val isBought: String,
    val isExamWord: String,
    val isFavourite: String,
    val isChecked: String,
    val isDefault: String
)