package com.example.bluetooth.database.models

import androidx.room.Entity

@Entity(tableName = "best_scores", primaryKeys = ["playerName", "levelId"])
class BestScore(
    val playerName: String,
    val levelId: Int
) {
    var collisions: Int = -1
}
