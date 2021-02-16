package com.example.bluetooth.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bluetooth.utils.Constant
import com.example.bluetooth.utils.uniformTransform

@Entity(tableName = "levels")
data class Level(
    @PrimaryKey val levelId: Int,
    val speed: Int,
    val frequency: Int,
    val distance: Int
) {
    val threshold: Int
        get() {
            val delay = frequency.uniformTransform(1, 10, Constant.freqMax, Constant.freqMin)

            val duration = Constant.distMultiplicand * distance / (speed.toFloat() * Constant.speedMultiplicand)

            val qtObstacles = duration / delay

            val finalThreshold = (qtObstacles / 3).toInt() + 1

            return if (finalThreshold < 3) 3 else finalThreshold
        }
}
