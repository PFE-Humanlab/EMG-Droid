package com.example.bluetooth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bluetooth.database.models.Player

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players")
    fun getAll(): List<Player>

    @Query("SELECT name FROM players")
    suspend fun getAllNames(): MutableList<String>

    @Query("SELECT * FROM players WHERE name = :name")
    suspend fun getPlayerByName(name : String) : Player

    @Insert
    suspend fun insertAll(vararg players: Player)

    @Update
    fun updatePlayers(vararg players: Player)

}