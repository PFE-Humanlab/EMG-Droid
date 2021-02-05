package com.example.bluetooth.game.objects.actual

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.Updatable
import kotlin.random.Random

class Obstacle(private val gameLogic: GameLogic, image: Bitmap) : BitmapDrawable(image), Updatable {

    private var alreadyCollided: Boolean = false

    var active: Boolean = false

    private var rotationSpeed: Int = 180
    private var rotation: Int = 0
    private var rotationDirection: Int = 1

    init {
        x = (-1 * w).toFloat()
    }

    override fun tickUpdate(deltaTimeMillis: Long) {

        if (active) {

            x -= gameLogic.currentSpeed * deltaTimeMillis / 1000

            if (x + w < 0) {
                active = false
            }

            rotation += rotationDirection * rotationSpeed * deltaTimeMillis.toInt() / 1000

        } else {
            rotationDirection *= -1
        }
    }

    override fun draw(canvas: Canvas) {
        if (active) {
//            super.draw(canvas)
            super.drawRotated(canvas, rotation.toFloat())
        }
    }

    fun reset() {
        alreadyCollided = false
        active = true
        x = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        y = Random.nextFloat() * (Resources.getSystem().displayMetrics.heightPixels - h)
    }


    override fun doIntersect(target: Intersectable): Boolean {
        val intersect = super.doIntersect(target)

        if (intersect && !alreadyCollided) {
            alreadyCollided = true
            gameLogic.handleCollision()
        }
        return intersect
    }
}