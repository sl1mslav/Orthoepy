package com.example.orthoepy.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = true)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun dictionaryDao() : DictionaryDao

    companion object {
        @Volatile
        private var INSTANCE: DictionaryDatabase? = null

        fun getDatabase(context: Context): DictionaryDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryDatabase::class.java,
                    "dictionary"
                )
                    .createFromAsset("database/dictionary.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}