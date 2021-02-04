package com.example.bluetooth.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.example.bluetooth.activity.EndGameActivity
import com.example.bluetooth.game.objects.actual.FinishLine
import com.example.bluetooth.game.objects.actual.GroupObstacles
import com.example.bluetooth.game.objects.actual.GroupStars
import com.example.bluetooth.game.objects.actual.Player
import com.example.bluetooth.game.objects.interf.Drawable
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.game.objects.interf.Updatable
import com.example.bluetooth.utils.leftPad
import com.example.bluetooth.utils.resizedBitmap
import com.example.bluetooth.utils.rotatedBitmap
import kotlin.math.max

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {

    private var thread: GameThread? = null
    var paused: Boolean = false

    // Intent params
    var minValue: Int? = null
    var maxValue: Int? = null
    var speed: Int? = null
    var distance: Int? = null
    var delay: Int? = null
    var endless: Boolean? = null
    var activity: AppCompatActivity? = null

    // Input Vars
    private var needUpdate: Boolean = false
    private var newInput: Int = 0

    // Game vars
    var currentSpeed: Int = 0
    var currentPos: Int = 0

    private var collisionPenalty: Int = 500
    private var collisionEffectTimer: Int = 0
    private var collisionsCount: Int = 0
    private val collisionEffectDuration: Int = 100

    private var startTime: Long = 0

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

//        this.setZOrderOnTop(true)

//        holder.setFormat(PixelFormat.TRANSPARENT)
    }

    fun handleCollision() {
        collisionsCount++
        currentSpeed = max(currentSpeed - collisionPenalty, 0)
        collisionEffectTimer = collisionEffectDuration
    }

    fun initGame() {
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

        speed?.let {
            collisionPenalty = (it * 2) / 3
        }

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

        val finishLine = FinishLine(this, finishBitmap)

        // register the game objects
        listDrawable.apply {
            add(Pair(backgroundStar, -1))
            add(Pair(groupObstacles, 0))
            if (endless == false) {
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
            if (endless == false) {
                add(finishLine)
            }
        }

        listPlayerUpdatable.add(player)


    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        setWillNotDraw(false)
        paused = false

        // instantiate the game thread
        thread = GameThread(holder, this)

        // start the game thread
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i("GameLoop", "surfaceChanged: ")
    }

    fun stopAndJoinThread() {
        pauseGame()

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
        pauseGame()
        stopAndJoinThread()

    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update(deltaTimeMillis: Long) {
        // update Speed
        if (paused) {
            return
        }

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
        if ((endless == true && collisionsCount > 10)
            || (endless == false && distance != null && currentPos > distance!!)
        ) {
            endGame()
        }


    }

    private fun pauseGame() {
        paused = true
    }

    private fun endGame() {

        pauseGame()


        stopAndJoinThread()


        val intent = Intent(context, EndGameActivity::class.java)

        intent.putExtra("collision", collisionsCount)
        intent.putExtra("timeMillis", (System.nanoTime() / 1000000) - startTime)

        // For try again, originals values :
        intent.putExtra("speed", speed ?: 10)
        intent.putExtra("distance", distance ?: 10)
        intent.putExtra("delay", delay ?: 500)
        intent.putExtra("min", minValue ?: 0)
        intent.putExtra("max", maxValue ?: 700)
        intent.putExtra("endless", endless ?: false)

        context.startActivity(intent)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Todo : don't forget to remove after testing

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
        // Todo : don't forget to uncomment after testing
//        newInput = value
//        needUpdate = true
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        // Draw game objects
        super.draw(canvas)

        if (paused) {
            Log.i("EndGame", "endGame: draw after end ")
            return
        }


        listDrawable.forEach {
            it.first.draw(canvas)
        }

        // update overlay

        val delta = (System.nanoTime() / 1000000) - startTime

        val minutes = (delta / 1000) / 60
        val seconds = (delta / 1000) % 60

        activity?.runOnUiThread {

            val timeText = activity?.findViewById<TextView>(R.id.timeText)

            timeText?.let {
                it.text = activity?.getString(
                    R.string.time_holder,
                    minutes.toString().leftPad(2, "0"),
                    seconds.toString().leftPad(2, "0")
                ) ?: ""
            }

            val collText = activity?.findViewById<TextView>(R.id.collText)

            collText?.let {
                it.text = collisionsCount.toString()
            }
        }

    }


}