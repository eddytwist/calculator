package com.eddysproject.calculator.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History (@ColumnInfo(name = "result") val result: String) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}