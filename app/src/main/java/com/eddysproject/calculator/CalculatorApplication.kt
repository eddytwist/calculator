package com.eddysproject.calculator

import android.app.Application
import androidx.room.Room

class CalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
    }

    companion object {
        var application: CalculatorApplication? = null
        var db: AppDatabase? = null
    }
}