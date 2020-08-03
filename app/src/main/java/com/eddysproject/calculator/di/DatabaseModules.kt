package com.eddysproject.calculator.di

import androidx.room.Room
import com.eddysproject.calculator.db.AppDatabase
import org.koin.dsl.module

private const val DB_NAME = "database-name"
val databaseModules = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, DB_NAME).build() }
    single { get<AppDatabase>().historyDao() }
}