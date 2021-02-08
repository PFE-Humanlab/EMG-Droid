package com.example.bluetooth.database.dao

import androidx.room.*
import com.example.bluetooth.database.models.Player
import com.example.bluetooth.database.models.jointure.PlayerWithRecord
import com.example.bluetooth.database.models.jointure.PlayerWithScore

@Dao
interface PlayerDAO {

    @Query("SELECT * FROM players")
    suspend fun getAll(): List<Player>

    @Query("SELECT playerName FROM players")
    suspend fun getAllNames(): MutableList<String>

    @Query("SELECT * FROM players WHERE playerName = :name")
    suspend fun getPlayerByName(name: String): Player

    @Insert
    suspend fun insertAll(vararg players: Player)

    @Update
    suspend fun updatePlayers(vararg players: Player)

    @Transaction
    @Query("SELECT * FROM players WHERE playerName = :name")
    suspend fun getPlayerWithRecord(name: String): PlayerWithRecord

    @Transaction
    @Query("SELECT * FROM players WHERE playerName = :name")
    suspend fun getPlayerWithScore(name: String): PlayerWithScore


}