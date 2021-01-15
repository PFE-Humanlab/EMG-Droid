package com.example.bluetooth.game.objects.abstracs

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.bluetooth.game.GameView

abstract class Drawable(private var image: Bitmap) {

    val w: Int = image.width
    val h: Int = image.height
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    var x: Float
    var y: Float

    init {
        x = screenWidth / 2f
        y = screenHeight / 2f

        GameView.listDrawable.add(this)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x, y, null)
    }

}