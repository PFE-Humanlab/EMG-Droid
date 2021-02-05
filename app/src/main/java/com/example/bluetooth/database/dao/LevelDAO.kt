package com.example.bluetooth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bluetooth.database.models.Level

@Dao
interface LevelDAO {

    @Query("SELECT * FROM levels")
    suspend fun getAll(): List<Level>

    @Insert
    suspend fun insertAll(vararg levels: Level)

}