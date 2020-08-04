package com.eddysproject.calculator.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eddysproject.calculator.db.data.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}