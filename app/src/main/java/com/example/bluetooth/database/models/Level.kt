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
            val delay = frequency
                .uniformTransform(1, 10, Constant.freqMax, Constant.freqMin)
                .toFloat() / 1000

            val distPixel = Constant.distMultiplicand * distance
            val speedPixelPerSec =
                speed.uniformTransform(1, 10, Constant.speedMin, Constant.speedMax) / 2

            val durationSec = distPixel / speedPixelPerSec.toFloat()

            val qtObstacles = durationSec / delay

            var finalThreshold = (qtObstacles / 3).toInt() + 1

            if (finalThreshold < 3) {
                finalThreshold = 3
            }

            return finalThreshold
        }
}
