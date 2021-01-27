package com.example.bluetooth.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.bluetooth.R
import com.example.bluetooth.game.objects.actual.GroupObstacles
import com.example.bluetooth.game.objects.actual.Obstacle
import com.example.bluetooth.game.objects.actual.Player
import com.example.bluetooth.game.objects.interf.Drawable
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.game.objects.interf.Updatable
import kotlin.math.max

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {

    private var thread: GameThread? = null

    private var needUpdate: Boolean = false
    private var newInput: Int = 0

    var minValue: Int? = null
    var maxValue: Int? = null
    var speed: Int? = null
    var distance: Int? = null

    var currentSpeed: Int = 0
    var currentPos: Int = 0

    private val collisionPenalty: Int = 500
    private var collisionEffectTimer: Int = 0
    private var collisionsCount: Int = 0
    private val collisionEffectDuration = 100


    companion object {
        val listUpdatable: MutableList<Updatable> = mutableListOf()
        val listDrawable: MutableList<Pair<Drawable, Int>> = mutableListOf()
        val listPlayersIntersectables: MutableList<Intersectable> = mutableListOf()
        val listObstaclesIntersectables: MutableList<Intersectable> = mutableListOf()
        val listPlayerUpdatable: MutableList<PlayerUpdatable> = mutableListOf()
    }

    init {
        // add callback
        holder.addCallback(this)
    }

    fun handleCollision() {
        collisionsCount++

        Log.i("Collision", "handleCollision: nb Collisions : $collisionsCount")
        Log.i("Collision", "handleCollision: pos : $currentPos")
        Log.i("Collision", "handleCollision: dist : $distance")
        Log.i("Collision", "handleCollision: speed : $currentSpeed")

        currentSpeed = max(currentSpeed - collisionPenalty, 0)

        Log.i("Collision", "handleCollision: speed : $currentSpeed")

        collisionEffectTimer = collisionEffectDuration
    }

    fun initGame() {
        // init lists
        listDrawable.removeAll { true }
        listPlayersIntersectables.removeAll { true }
        listObstaclesIntersectables.removeAll { true }
        listUpdatable.removeAll { true }
        listPlayerUpdatable.removeAll { true }


        currentSpeed = 0
        currentPos = 0
        collisionsCount = 0
        collisionEffectTimer = 0

//         Setup the game objects
        val groupObstacles = GroupObstacles(
            listOf(
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade)),
                Obstacle(this, BitmapFactory.decodeResource(resources, R.drawable.grenade))
            )
        )

        val player = Player(this, BitmapFactory.decodeResource(resources, R.drawable.player))

        // register the game objects
        listDrawable.apply {
            add(Pair(groupObstacles, 0))
            add(Pair(player, 1))
        }

        listObstaclesIntersectables.add(groupObstacles)
        listPlayersIntersectables.add(player)

        listUpdatable.add(groupObstacles)
        listUpdatable.add(player)

        listPlayerUpdatable.add(player)

    }


    override fun surfaceCreated(holder: SurfaceHolder) {

        setWillNotDraw(false)

        // instantiate the game thread
        thread = GameThread(holder, this)

        // start the game thread
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i("GameLoop", "surfaceChanged: ")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

        var retry = true
        while (retry) {

            try {
                thread?.setRunning(false)
                thread?.join()

                Log.i("GameView", "surfaceDestroyed: setRunning = joined")
                retry = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update(deltaTimeMillis: Long) {
        // update Speed

        speed?.let { wantedSpeed ->

            val acceleration = (wantedSpeed - currentSpeed)

            currentSpeed += acceleration * deltaTimeMillis.toInt() / 1000

            currentPos += currentSpeed * deltaTimeMillis.toInt() / 1000

        }

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
        distance?.let {

            if (currentPos > it) {
                endGame()
            }
        }
    }

    private fun endGame() {
        Log.i("GameLoop", "endGame: Not Implemented")
        TODO("Not yet implemented")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            val yTouched = event.y.toInt()
            minValue = 0
            maxValue = 700

            val newValue = yTouched / Resources.getSystem().displayMetrics.heightPixels.toFloat()
            newInput = (newValue * maxValue!!).toInt()
            needUpdate = true
        }

        return super.onTouchEvent(event)
    }

    fun updatePlayer(value: Int) {
//        newInput = value
//        needUpdate = true
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        // Draw game objects

        super.draw(canvas)

        listDrawable.sortBy { it.second }

        listDrawable.forEach {
            it.first.draw(canvas)
        }


    }


}