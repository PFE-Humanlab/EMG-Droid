package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import com.example.bluetooth.game.GameView
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.PlayerUpdatable

class Player(private val gameView: GameView, image: Bitmap) : BitmapDrawable(image),
    PlayerUpdatable {

    var destination: Float = 0f

    private val speed: Float = screenHeight.toFloat()

    override fun playerUpdate(value: Int) {

        x = 32f

        if (gameView.minValue == null || gameView.maxValue == null) {
            return
        }
        // Value by Min, max mapped to 32, screenHeight - 32 - w
        val topOffset = 32
        val botOffset = 32
        val factor = (screenHeight - topOffset - botOffset)

        val centered = value - gameView.minValue!!
        val reduced = centered / gameView.maxValue!!.toFloat()
        val sized = reduced * factor
        destination = topOffset + sized

    }

    override fun tickUpdate(deltaTimeMillis: Long) {

        if (y + h < destination) { // si au dessus de la destination

            y += speed * deltaTimeMillis / 1000

        } else if (y > destination) { // si en dessous de la destination

            y -= speed * deltaTimeMillis / 1000

        } else { // si destination sur moi

            // do Nothing

        }
    }

}