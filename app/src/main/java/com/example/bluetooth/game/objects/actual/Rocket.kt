package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.utils.uniformTransform

class Rocket(private val minValue: Int, private val maxValue: Int, image: Bitmap) :
    BitmapDrawable(image), PlayerUpdatable {

    private var destination: Float = 0f

    private val speed: Float = screenHeight.toFloat() / 1.3f

    private val topOffset = 96
    private val botOffset = 96

    private val leftOffset = 32

    override fun playerUpdate(value: Int) {
        destination = value.toFloat().uniformTransform(
            minValue.toFloat(),
            maxValue.toFloat(),
            (screenHeight - botOffset).toFloat(),
            topOffset.toFloat()
        )
    }

    override fun tickUpdate(deltaTimeMillis: Long) {
        // snap to boundary
        val topFloat = topOffset.toFloat()
        if (y < topFloat) {
            y = topFloat
        }
        val botFloat = (screenHeight - h - botOffset).toFloat()
        if (y + h > botFloat) {
            y = botFloat
        }
        x = leftOffset.toFloat()
        // update position : go to destination
        if (y + h < destination) {
            y += speed * deltaTimeMillis / 1000
        } else if (y > destination) {
            y -= speed * deltaTimeMillis / 1000
        }
    }

}