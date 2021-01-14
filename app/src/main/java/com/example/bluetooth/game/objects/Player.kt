package com.example.bluetooth.game.objects

import android.graphics.Bitmap
import android.graphics.Canvas

class Player(image: Bitmap) : GameObject(image) {

    var x: Int = 0
    var y: Int = 0

    init {
        x = screenWidth / 2
        y = screenHeight - 200
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    override fun update() {
        // Nothing !!
    }

    fun updateTouch(touch_x: Int, touch_y: Int) {
        x = touch_x - w / 2
        y = touch_y - h / 2
    }
}