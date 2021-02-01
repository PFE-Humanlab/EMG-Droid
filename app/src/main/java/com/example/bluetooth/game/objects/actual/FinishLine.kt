package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.GameView
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.Updatable

class FinishLine(val gView: GameView, image: Bitmap) : BitmapDrawable(image), Updatable {

    init {
        y = 0f
    }

    override fun tickUpdate(deltaTimeMillis: Long) {

        gView.distance?.let {
            x = (it - gView.currentPos).toFloat()
        }
    }

}