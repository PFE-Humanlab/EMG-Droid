package com.example.bluetooth.game

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) :
    Thread() {

    @Volatile
    private var running: Boolean = false

    // frames per second, the rate at which you would like to refresh the Canvas
    private val targetFPS = 50

    fun setRunning(isRunning: Boolean) {
        synchronized(running) {
            running = isRunning
        }
    }

    override fun run() {
        var startTime: Long
        var startTimeOld: Long = System.nanoTime()
        var timeMillis: Long
        var waitTime: Long

        var timeLastUpdate: Long = System.nanoTime()
        var timeUpdate: Long

        val targetTime = (1000 / targetFPS).toLong()

        while (running) {

            var canvas: Canvas? = null

            try {
                // locking the canvas allows us to draw on to it
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    synchronized(surfaceHolder) {
                        timeUpdate = System.nanoTime()
                        gameView.update((timeUpdate - timeLastUpdate) / 1000000)
                        timeLastUpdate = timeUpdate
                        gameView.drawGame(canvas)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            startTime = System.nanoTime()

            timeMillis = (startTime - startTimeOld) / 1000000
            waitTime = targetTime - timeMillis

            startTimeOld = startTime
            if (waitTime > 0) {
                try {
                    sleep(waitTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
