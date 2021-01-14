package com.example.bluetooth.game.objects

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

abstract class GameObject(var image: Bitmap) {

    val w: Int
    val h: Int
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height
    }

    abstract fun draw(canvas: Canvas)

    abstract fun update()

}