package com.example.orthoepy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "wordText")
    val wordText: String,
    @ColumnInfo(name = "wordTextStress")
    val wordTextStress: String,
    @ColumnInfo(name = "stressIndex")
    val stressIndex: Int,
    @ColumnInfo(name = "memorizationLevel")
    val memorizationLevel: Int,

    @ColumnInfo(name = "isBought")
    var isBought: String,
    @ColumnInfo(name = "isExamWord")
    val isExamWord: String,
    @ColumnInfo(name = "isFavourite")
    var isFavourite: String,
    @ColumnInfo(name = "isChecked")
    var isChecked: String,
    @ColumnInfo(name = "isDefault")
    val isDefault: String
)