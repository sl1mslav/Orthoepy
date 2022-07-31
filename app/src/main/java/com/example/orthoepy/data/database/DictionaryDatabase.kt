package com.example.orthoepy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.orthoepy.entity.Word

@Database(entities = [Word::class], version = 1, exportSchema = true)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao() : DictionaryDao
}