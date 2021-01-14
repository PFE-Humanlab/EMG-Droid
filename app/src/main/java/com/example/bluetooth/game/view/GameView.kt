package com.example.bluetooth.game.view

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
import com.example.bluetooth.game.objects.MovingObstacle
import com.example.bluetooth.game.objects.Player

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private val thread: GameThread

    private var player: Player? = null

    private var touched: Boolean = false
    private var touched_x: Int = 0
    private var touched_y: Int = 0


    private var movingObstacle: MovingObstacle? = null

    init {
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Setup the game objects
        movingObstacle = MovingObstacle(BitmapFactory.decodeResource(resources, R.drawable.grenade))
        player = Player(BitmapFactory.decodeResource(resources, R.drawable.player))

        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update() {
        // update game objects

        movingObstacle!!.update()

        if (touched) {
            player!!.updateTouch(touched_x, touched_y)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        touched_x = event.x.toInt()
        touched_y = event.y.toInt()

        val action = event.action
        touched = when (action) {
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
        movingObstacle!!.draw(canvas)
        player!!.draw(canvas)

    }

}