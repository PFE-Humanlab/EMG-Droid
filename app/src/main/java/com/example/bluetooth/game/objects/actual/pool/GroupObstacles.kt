package com.example.bluetooth.game.objects.actual.pool

import android.graphics.Bitmap
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.PoolGameObjects
import com.example.bluetooth.game.objects.actual.Obstacle
import com.example.bluetooth.game.objects.interf.Updatable
import kotlin.random.Random

class GroupObstacles(private val gameLogic: GameLogic, delayObstacles : Int, private val image: Bitmap) :
    PoolGameObjects<Obstacle>(), Updatable {

    override val list: MutableList<Obstacle> = mutableListOf()

    private val delayBetweenObstacles: Int = delayObstacles

    private var delay: Int = 0

    override var xDraw: Float = 0f
    override var yDraw: Float = 0f

    override fun tickUpdate(deltaTimeMillis: Long) {

        delay -= deltaTimeMillis.toInt()

        if (delay < 0) {
            var availableObstacle = list.find { !it.active }

            if (availableObstacle == null) {
                availableObstacle = Obstacle(gameLogic, image)
                list.add(availableObstacle)
            }

            availableObstacle.reset()
            val randomBetween09And11 = (0.9 * Random.nextFloat() + 0.2)
            delay = (delayBetweenObstacles * randomBetween09And11).toInt()

        }

        list.forEach {
            it.tickUpdate(deltaTimeMillis)
        }
    }

}