package com.example.bluetooth.game

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.bluetooth.R
import com.example.bluetooth.game.objects.actual.FinishLine
import com.example.bluetooth.game.objects.actual.Rocket
import com.example.bluetooth.game.objects.actual.pool.GroupObstacles
import com.example.bluetooth.game.objects.actual.pool.GroupStars
import com.example.bluetooth.game.objects.interf.Drawable
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.game.objects.interf.Updatable
import com.example.bluetooth.utils.resizedBitmap
import com.example.bluetooth.utils.rotatedBitmap
import kotlin.math.max

class GameLogic(
    resources: Resources,
    private val speed: Int,
    distance: Int,
    private val endless: Boolean,
    minValue: Int,
    maxValue: Int,
    delayOfObstacles: Int,
    val endGame: () -> Unit
) {

    var currentSpeed: Int = 0

    var currentPos: Int = 0
    var collisionsCount: Int = 0

    var startTime: Long = 0

    val listDrawable: MutableList<Pair<Drawable, Int>> = mutableListOf()

    private val rocket: Rocket

    private val listUpdatable: MutableList<Updatable> = mutableListOf()

    private val listIntersectables: MutableList<Intersectable> = mutableListOf()
    private val listPlayerUpdatable: MutableList<PlayerUpdatable> = mutableListOf()

    private var collisionPenalty: Int = 500

    private var paused: Boolean = false
    private var needUpdate: Boolean = false
    private var newInput: Int = 0

    init {

        // init lists
        listDrawable.removeAll { true }
        listIntersectables.removeAll { true }
        listUpdatable.removeAll { true }
        listPlayerUpdatable.removeAll { true }

        startTime = System.nanoTime() / 1000000
        currentSpeed = 0
        currentPos = 0
        collisionsCount = 0

        paused = false

        collisionPenalty = (speed * 2) / 3

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // load images
        val obstacleBitmap = BitmapFactory
            .decodeResource(resources, R.drawable.asteroide)
            .resizedBitmap(screenHeight / 15)
        val playerBitmap = BitmapFactory.decodeResource(resources, R.drawable.fusee)
            .resizedBitmap(screenHeight / 10)
            .rotatedBitmap(90f)

        val finishBitmap = BitmapFactory
            .decodeResource(resources, R.drawable.finish)
            .resizedBitmap(screenHeight)

        val listStars = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.star4)
                .resizedBitmap(screenHeight / 50),
            BitmapFactory.decodeResource(resources, R.drawable.star5)
                .resizedBitmap(screenHeight / 50),
        )

//         Setup the game objects
        rocket = Rocket(minValue, maxValue, playerBitmap)
        val groupObstacles = GroupObstacles(this, delayOfObstacles, obstacleBitmap)
        val backgroundStar = GroupStars(this, delayOfObstacles, listStars)

        if (!endless) {
            val finishLine = FinishLine(this, distance, endGame, finishBitmap)
            listDrawable.add(Pair(finishLine, 0))
            listUpdatable.add(finishLine)
            listIntersectables.add(finishLine)
        }

        // register the game objects
        listDrawable.apply {
            add(Pair(backgroundStar, -1))
            add(Pair(groupObstacles, 0))
            add(Pair(rocket, 1))
        }

        listDrawable.sortBy { it.second }

        listIntersectables.add(groupObstacles)

        listUpdatable.apply {
            add(backgroundStar)
            add(groupObstacles)
            add(rocket)
        }

        listPlayerUpdatable.add(rocket)
    }

    fun update(deltaTimeMillis: Long) {

        // update Speed
        if (paused) {
            return
        }

        val acceleration = (speed - currentSpeed)

        currentSpeed += acceleration * deltaTimeMillis.toInt() / 1000

        currentPos += currentSpeed * deltaTimeMillis.toInt() / 1000

        // update game objects
        if (needUpdate) {
            needUpdate = false

            listPlayerUpdatable.forEach {
                it.playerUpdate(newInput)
            }
        }

        listUpdatable.forEach {
            it.tickUpdate(deltaTimeMillis)
        }

        // Check for intersections for updated objects
        listIntersectables.forEach {
            rocket.doIntersect(it)
            it.doIntersect(rocket)
        }

        // Check if game is over
        if (endless && collisionsCount >= 10) {
            endGame()
        }
    }

    fun setPause(paused: Boolean) {
        this.paused = paused
    }

    fun updatePlayer(value: Int) {
        newInput = value
        needUpdate = true
    }

    fun handleCollision() {
        collisionsCount++
        currentSpeed = max(currentSpeed - collisionPenalty, 0)
    }
}
