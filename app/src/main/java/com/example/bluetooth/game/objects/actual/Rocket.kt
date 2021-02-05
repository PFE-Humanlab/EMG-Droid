package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import android.util.Log
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable

class Rocket(private val minValue: Int, private val maxValue: Int, image: Bitmap) : BitmapDrawable(image),
    PlayerUpdatable {

    private var destination: Float = 0f

    private val speed: Float = screenHeight.toFloat()

    private val topOffset = 32
    private val botOffset = 32

    override fun playerUpdate(value: Int) {

        val factor = (screenHeight - topOffset - botOffset)

        // value € [min , Max]
        // [min , Max] - min => [0, max - min]
        val centered = value - minValue
        // [0, max - min] / (max - min) => [0, 1]
        val reduced = centered / (maxValue - minValue).toFloat()
        // [0, 1] * (screenHeight - topOffset - botOffset) => [0, screenHeight - topOffset - botOffset]
        val sized = reduced * factor

        // [0, screenHeight - topOffset - botOffset] + topOffset => [topOffset, Height - botOffset]
        destination = sized + topOffset


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