package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable
import com.example.bluetooth.utils.uniformTransform

class Rocket(private val minValue: Int, private val maxValue: Int, image: Bitmap) :
    BitmapDrawable(image),
    PlayerUpdatable {

    private var destination: Float = 0f

    private val speed: Float = screenHeight.toFloat()

    private val topOffset = 32
    private val botOffset = 32

    override fun playerUpdate(value: Int) {

        // [0, screenHeight - topOffset - botOffset] + topOffset => [topOffset, Height - botOffset]
        destination = value.toFloat().uniformTransform(
            minValue.toFloat(),
            maxValue.toFloat(),
            topOffset.toFloat(),
            (screenHeight - botOffset).toFloat()
        )

    }

    override fun tickUpdate(deltaTimeMillis: Long) {


        if (y < 0) {
            y = topOffset.toFloat()
        }
        if (y + w > screenHeight) {
            y = (screenHeight - w - botOffset).toFloat()
        }
        x = 32f



        if (y + h < destination) { // si au dessus de la destination

            y += speed * deltaTimeMillis / 1000

        } else if (y > destination) { // si en dessous de la destination

            y -= speed * deltaTimeMillis / 1000

        }

    }

}