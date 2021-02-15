package com.example.bluetooth.game.objects.actual

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.Updatable
import com.example.bluetooth.utils.uniformTransform
import kotlin.random.Random

class BackgroundStar(private val gameLogic: GameLogic, image: Bitmap) :
    BitmapDrawable(image), Updatable {

    var active: Boolean = false

    private val parallaxFactor: Float

    init {
        x = (-1 * w).toFloat()
        parallaxFactor = Random.nextFloat()
            .uniformTransform(0f, 1f, 0.4f, 0.6f)
    }

    override fun tickUpdate(deltaTimeMillis: Long) {
        if (active) {
            val speed = gameLogic.currentSpeed * deltaTimeMillis / 1000
            x -= speed * parallaxFactor
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
