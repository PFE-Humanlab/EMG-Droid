package com.example.bluetooth.game.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.bluetooth.R
import com.example.bluetooth.game.GameThread
import com.example.bluetooth.game.objects.abstracs.Drawable
import com.example.bluetooth.game.objects.abstracs.Intersectable
import com.example.bluetooth.game.objects.abstracs.PlayerUpdatable
import com.example.bluetooth.game.objects.abstracs.Updatable
import com.example.bluetooth.game.objects.example.MovingObstacle
import com.example.bluetooth.game.objects.example.Player

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private var thread: GameThread? = null

    private var player: Player? = null
    private var movingObstacle: MovingObstacle? = null

    private var needUpdate: Boolean = false
    private var touchedX: Int = 0
    private var touchedY: Int = 0


    init {
        // add callback
        holder.addCallback(this)
    }

    private fun initGame() {
        // init lists
        listDrawable.removeAll { true }
        listIntersectable.removeAll { true }
        listUpdatable.removeAll { true }
        listPlayerUpdatable.removeAll { true }

        // Setup the game objects
        movingObstacle = MovingObstacle(BitmapFactory.decodeResource(resources, R.drawable.grenade))
        player = Player(BitmapFactory.decodeResource(resources, R.drawable.player))

        // register the game objects : todo : automatic (done for drawable and intersectable)
        listUpdatable.apply {
            add(movingObstacle!!)
        }
        listPlayerUpdatable.apply {
            add(player!!)
        }
    }

//    val TAG = "GameView"

    override fun surfaceCreated(holder: SurfaceHolder) {
        initGame()

//        Log.i(TAG, "surfaceCreated: ")

        // instantiate the game thread
        thread = GameThread(holder, this)

        // start the game thread
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//        Log.i(TAG, "surfaceChanged: ")

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
//        Log.i(TAG, "surfaceDestroyed: ")

        var retry = true
        while (retry) {
//            Log.i(TAG, "surfaceDestroyed: While retry")
            try {
                thread?.setRunning(false)
//                Log.i(TAG, "surfaceDestroyed: setRunning = false")
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
        // update game objects
        listUpdatable.forEach {
            it.tickUpdate(deltaTimeMillis)
        }

        if (needUpdate) {
            listPlayerUpdatable.forEach {
                it.playerUpdate(touchedX, touchedY)
            }
        }

        listIntersectable.forEachIndexed { indexOuter, itOuter ->
            listIntersectable.filterIndexed { index, it ->
                indexOuter > index && itOuter.doIntersect(it)
            }.forEach {
                itOuter.intersect(it)
                it.intersect(itOuter)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        // Todo : change to bluetooth data thread

        touchedX = event.x.toInt()
        touchedY = event.y.toInt()

        val action = event.action
        needUpdate = when (action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_MOVE -> true

            else -> false
        }

        return true
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // Draw game objects
        listDrawable.forEach {
            it.draw(canvas)
        }

    }

    companion object {
        val listUpdatable: MutableList<Updatable> = mutableListOf()
        val listDrawable: MutableList<Drawable> = mutableListOf()
        val listIntersectable: MutableList<Intersectable> = mutableListOf()
        val listPlayerUpdatable: MutableList<PlayerUpdatable> = mutableListOf()
    }

}