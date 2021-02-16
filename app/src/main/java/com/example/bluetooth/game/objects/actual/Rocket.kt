package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.utils.uniformTransform

class Rocket(private val minValue: Int, private val maxValue: Int, image: Bitmap) :
    BitmapDrawable(image), PlayerUpdatable {

    private var destination: Float = 0f
    private val speed: Float = screenHeight.toFloat() / 1.3f

    private val topOffset = 128
    private val botOffset = 128
    private val topToBotOffset = screenHeight - botOffset

    private val leftOffset = 32

    override fun playerUpdate(value: Int) {
        destination = value.uniformTransform(
            minValue,
            maxValue,
            topToBotOffset,
            topOffset
        ).toFloat()
    }

    override fun tickUpdate(deltaTimeMillis: Long) {
        // snap to boundary
        val topFloat = topOffset.toFloat()
        val topToBotFloat = topToBotOffset.toFloat()

        if (y < topFloat) {
            y = topFloat
        } else if (y + h > topToBotFloat) {
            y = topToBotFloat - h
        }

        x = leftOffset.toFloat()

        // update position : go to destination
        val amountToMove = speed * deltaTimeMillis / 1000
        if (y + h < destination) {
            y += amountToMove
        } else if (y > destination) {
            y -= amountToMove
        }
    }
}
