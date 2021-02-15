package com.example.bluetooth.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class Level(
    @PrimaryKey val levelId: Int,
    val speed: Int,
    val delay: Int,
    val distance: Int
) {
    val threshold: Int
        get() {
            val duration = 10 * distance / speed.toFloat()

            val qtObstacles = duration * 2 / delay

            val finalThreshold = (qtObstacles / 3).toInt() + 1

            return if (finalThreshold > 3) 3 else finalThreshold
        }
}
