package com.eddysproject.calculator.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.eddysproject.calculator.db.data.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    suspend fun getAll(): List<History>

    @Query("SELECT * FROM history WHERE uid IN (:ids)")
    suspend fun loadAllByIds(ids: IntArray): List<History>

    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    suspend fun findByValue(result: String): History

    @Insert
    suspend fun insertAll(vararg histories: History)

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}