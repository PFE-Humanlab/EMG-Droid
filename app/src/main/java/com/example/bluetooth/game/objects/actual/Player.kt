package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable

class Player(private val gameLogic: GameLogic, image: Bitmap) : BitmapDrawable(image),
    PlayerUpdatable {

    private var destination: Float = 0f

    private val speed: Float = screenHeight.toFloat()

    private val topOffset = 32
    private val botOffset = 32

    override fun playerUpdate(value: Int) {

        // Value by Min, max mapped to 32, screenHeight - 32 - w
        val factor = (screenHeight - topOffset - botOffset)

        val centered = value - gameLogic.minValue
        val reduced = centered / gameLogic.maxValue.toFloat()
        val sized = reduced * factor
        destination = topOffset + sized

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