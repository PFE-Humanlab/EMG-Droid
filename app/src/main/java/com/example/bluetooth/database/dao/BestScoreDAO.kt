package com.example.bluetooth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bluetooth.database.models.BestScore

@Dao
interface BestScoreDAO {

    @Insert
    suspend fun insertAll(vararg bestScores: BestScore)

    @Query("SELECT * FROM best_scores WHERE playerName = :playerName")
    suspend fun getBestScoresByPlayerName(playerName : String): List<BestScore>

    @Query("SELECT * FROM best_scores WHERE playerName = :playerName AND levelId = :levelId")
    suspend fun getBestScore(playerName: String, levelId : Int): BestScore
}