package com.example.bluetooth.game

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import com.example.bluetooth.R
import com.example.bluetooth.activity.EndGameActivity
import com.example.bluetooth.activity.GameActivity
import com.example.bluetooth.utils.leftPad

class GameView(context: Context, attributes: AttributeSet) :
    SurfaceView(context, attributes),
    SurfaceHolder.Callback {

    private lateinit var gameLogic: GameLogic

    private var thread: GameThread? = null

    // Intent params : ste by activity
    var speed: Int? = null
    var endless: Boolean? = null
    var activity: GameActivity? = null
    var distance: Int? = null
    var minValue: Int? = null
    var maxValue: Int? = null
    var delay: Int? = null
    var levelId: Int? = null

    init {
        // add callback
        holder.addCallback(this)
    }

    fun initGame() {
        gameLogic = GameLogic(
            resources,
            speed!!,
            distance!!,
            endless!!,
            minValue!!,
            maxValue!!,
            delay!!
        ) { endGame() }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        setWillNotDraw(false)
        gameLogic.setPause(false)

        // instantiate the game thread
        thread = GameThread(holder, this)

        // start the game thread
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    fun stopAndJoinThread() {
        gameLogic.setPause(true)

        var retry = true

        while (retry) {

            try {
                thread?.setRunning(false)
                thread?.join(1000)
                retry = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLogic.setPause(true)
        stopAndJoinThread()
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update(deltaTimeMillis: Long) {
        gameLogic.update(deltaTimeMillis)
    }

    private fun endGame() {

        gameLogic.setPause(true)

        stopAndJoinThread()

        val intent = Intent(context, EndGameActivity::class.java)

        intent.putExtra("collision", gameLogic.collisionsCount)
        intent.putExtra("timeMillis", (System.nanoTime() / 1000000) - gameLogic.startTime)

        // For try again, originals values :
        intent.putExtra("speed", speed ?: 10)
        intent.putExtra("distance", distance ?: 10)
        intent.putExtra("delay", delay ?: 500)
        intent.putExtra("min", minValue ?: 0)
        intent.putExtra("max", maxValue ?: 700)
        intent.putExtra("endless", endless ?: false)
        intent.putExtra("playerName", activity!!.playerName)
        intent.putExtra("levelId", levelId)

        context.startActivity(intent)
    }

//    Debug : uncomment when you need to control with screen touch instead of EMG
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        if (event != null) {
//
//            gameLogic.updatePlayer(
//                event.y.uniformTransform(
//                    0f,
//                    Resources.getSystem().displayMetrics.heightPixels.toFloat(),
//                    minValue!!.toFloat(),
//                    maxValue!!.toFloat()
//                ).toInt()
//            )
//        }
//
//        return super.onTouchEvent(event)
//    }

    fun updatePlayer(value: Int) {
        gameLogic.updatePlayer(value)
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        // Draw game objects
        super.draw(canvas)

        gameLogic.listDrawable.forEach {
            it.first.draw(canvas)
        }

        // update overlay

        val delta = (System.nanoTime() / 1000000) - gameLogic.startTime

        val minutes = (delta / 1000) / 60
        val minutesString = minutes.toString().leftPad(2, "0")

        val seconds = (delta / 1000) % 60
        val secondsString = seconds.toString().leftPad(2, "0")

        activity?.runOnUiThread {

            val timeText = activity?.findViewById<TextView>(R.id.timeText)

            timeText?.let {
                it.text =
                    activity?.getString(R.string.time_holder, minutesString, secondsString) ?: ""
            }

            val collText = activity?.findViewById<TextView>(R.id.collText)

            collText?.let {
                it.text = gameLogic.collisionsCount.toString()
            }
        }
    }
}
