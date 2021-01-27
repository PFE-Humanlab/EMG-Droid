package com.example.bluetooth.game.objects.actual

import android.graphics.Canvas
import com.example.bluetooth.game.objects.abstracs.PoolGameObjects
import com.example.bluetooth.game.objects.interf.Updatable
import kotlin.random.Random

class GroupObstacles(val list: List<Obstacle>) : PoolGameObjects(list), Updatable {

    // Todo : in game View and calibration
    // easy : 5000, hard : 500
    private val delayBetweenObstacles: Int = 500

    private var delay: Int = 0

    override var xDraw: Float = 0f
    override var yDraw: Float = 0f

    override fun tickUpdate(deltaTimeMillis: Long) {

        delay -= deltaTimeMillis.toInt()

        if (delay < 0) {
            val availableObstacle = list.find { !it.active }

            availableObstacle?.let {

                it.reset()

                val randomBetween09And11 = (0.9 * Random.nextFloat() + 0.2)

                delay = (delayBetweenObstacles * randomBetween09And11).toInt()

            }
        }

        list.forEach {
            it.tickUpdate(deltaTimeMillis)
        }
    }

}