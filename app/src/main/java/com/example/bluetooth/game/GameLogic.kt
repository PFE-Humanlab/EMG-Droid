package com.example.bluetooth.game

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import com.example.bluetooth.R
import com.example.bluetooth.game.objects.actual.FinishLine
import com.example.bluetooth.game.objects.actual.GroupObstacles
import com.example.bluetooth.game.objects.actual.GroupStars
import com.example.bluetooth.game.objects.actual.Player
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
    private val distance: Int,
    private val endless: Boolean,
    val minValue: Int,
    val maxValue: Int,
    val delay: Int,
    val endGame: () -> Unit
) {


    var paused: Boolean = false

    var currentSpeed: Int = 0
    var currentPos: Int = 0


    var collisionsCount: Int = 0
    var startTime: Long = 0
    val listDrawable: MutableList<Pair<Drawable, Int>> = mutableListOf()

    private val listUpdatable: MutableList<Updatable> = mutableListOf()
    private val listPlayersIntersectables: MutableList<Intersectable> = mutableListOf()
    private val listObstaclesIntersectables: MutableList<Intersectable> = mutableListOf()
    private val listPlayerUpdatable: MutableList<PlayerUpdatable> = mutableListOf()

    private var collisionPenalty: Int = 500
    private var collisionEffectTimer: Int = 0
    private val collisionEffectDuration: Int = 100

    private var needUpdate: Boolean = false
    private var newInput: Int = 0

    init {
        Log.i("TAG", "init: Start Init Game Logic")

        // init lists
        listDrawable.removeAll { true }
        listPlayersIntersectables.removeAll { true }
        listObstaclesIntersectables.removeAll { true }
        listUpdatable.removeAll { true }
        listPlayerUpdatable.removeAll { true }

        startTime = System.nanoTime() / 1000000
        currentSpeed = 0
        currentPos = 0
        collisionsCount = 0
        collisionEffectTimer = 0

        paused = false

        collisionPenalty = (speed * 2) / 3

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // load images
        val obstacleBitmap = BitmapFactory
            .decodeResource(resources, R.drawable.asteroide)
            .resizedBitmap(screenHeight / 20)
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
        val groupObstacles = GroupObstacles(this, obstacleBitmap)
        val player = Player(this, playerBitmap)
        val backgroundStar = GroupStars(this, listStars)
        val finishLine = FinishLine(this, distance, finishBitmap)

        // register the game objects
        listDrawable.apply {
            add(Pair(backgroundStar, -1))
            add(Pair(groupObstacles, 0))
            if (!endless) {
                add(Pair(finishLine, 0))
            }
            add(Pair(player, 1))
        }

        listDrawable.sortBy { it.second }

        listObstaclesIntersectables.add(groupObstacles)
        listPlayersIntersectables.add(player)

        listUpdatable.apply {
            add(backgroundStar)
            add(groupObstacles)
            add(player)
            if (!endless) {
                add(finishLine)
            }
        }

        listPlayerUpdatable.add(player)

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

        //Check for intersections for updated objects
        listPlayersIntersectables.forEach { itOuter ->
            listObstaclesIntersectables.forEach {

                itOuter.doIntersect(it)
                it.doIntersect(itOuter)
            }
        }

        // Check if game is over
        if ((endless && collisionsCount > 10) || (!endless && currentPos > distance)
        ) {
            endGame()
        }


    }

    fun setPause(paused : Boolean) {
        this.paused = paused
    }

    fun updatePlayer(value: Int) {
        newInput = value
        needUpdate = true
    }

    fun handleCollision() {
        collisionsCount++
        currentSpeed = max(currentSpeed - collisionPenalty, 0)
        collisionEffectTimer = collisionEffectDuration
    }

}