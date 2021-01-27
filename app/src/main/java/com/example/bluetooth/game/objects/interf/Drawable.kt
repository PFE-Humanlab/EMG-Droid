package com.example.bluetooth.game.objects.interf

import android.graphics.Canvas

interface Drawable {

    val wDraw: Int
    val hDraw: Int

    var xDraw: Float
    var yDraw: Float

    fun draw(canvas: Canvas)

}