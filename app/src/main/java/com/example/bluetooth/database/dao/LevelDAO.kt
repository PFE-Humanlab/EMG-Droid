package com.example.bluetooth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bluetooth.database.models.Level

@Dao
interface LevelDAO {

    @Query("SELECT * FROM levels")
    suspend fun getAll(): List<Level>

    @Query("SELECT * FROM levels WHERE levelId = :levelId")
    suspend fun getById(levelId: Int): Level

    @Query("SELECT COUNT(*) FROM levels")
    suspend fun countAll(): Int

    @Insert
    suspend fun insertAll(vararg levels: Level)

}