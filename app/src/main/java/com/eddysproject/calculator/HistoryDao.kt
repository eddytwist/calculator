package com.eddysproject.calculator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Query("SELECT * FROM history WHERE uid IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<History>

    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    fun findByValue(result: String): History

    @Insert
    fun insertAll(vararg histories: History)

    @Delete
    fun delete(history: History)
}