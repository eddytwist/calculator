package com.eddysproject.calculator.ui.main

import com.eddysproject.calculator.db.HistoryDao
import com.eddysproject.calculator.db.data.History

class MainRepository(private val dao: HistoryDao) {
    suspend fun onClearHistory() {
        dao.deleteAll()
    }

    suspend fun insertAll(history: History) {
        dao.insertAll(history)
    }

    suspend fun getAll() = dao.getAll()
}