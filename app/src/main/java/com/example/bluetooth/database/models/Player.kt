package com.example.bluetooth.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(@PrimaryKey var playerName: String) {

    var maxLevel: Int = 1
    var bestEndless : Long = -1
}