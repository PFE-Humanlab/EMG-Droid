package com.example.bluetooth.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class Level(
    @PrimaryKey val levelId: Int,
    val speed: Int,
    val delay: Int,
    val distance: Int
)
