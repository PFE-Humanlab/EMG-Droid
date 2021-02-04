package com.example.bluetooth.game.objects.actual

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.bluetooth.game.GameView
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.Updatable
import kotlin.random.Random

class BackgroundStar(private val gameView: GameView, image: Bitmap) : BitmapDrawable(image),
    Updatable {

    var active: Boolean = false

    init {
        x = (-1 * w).toFloat()
    }

    override fun tickUpdate(deltaTimeMillis: Long) {
        if (active) {
            x -= gameView.currentSpeed * deltaTimeMillis / 1000
            if (x + w < 0) {
                active = false
            }
        }
    }


    override fun draw(canvas: Canvas) {
        if (active) {
            super.draw(canvas)
        }
    }

    fun reset() {
        active = true
        x = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        y = Random.nextFloat() * (Resources.getSystem().displayMetrics.heightPixels - h)
    }
}