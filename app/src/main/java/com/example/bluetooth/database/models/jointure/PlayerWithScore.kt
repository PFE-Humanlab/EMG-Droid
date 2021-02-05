package com.example.bluetooth.database.models.jointure

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level
import com.example.bluetooth.database.models.Player

data class PlayerWithScore(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "playerName",
        entityColumn = "levelId",
        associateBy = Junction(BestScore::class)
    )
    val songs: List<Level>
)