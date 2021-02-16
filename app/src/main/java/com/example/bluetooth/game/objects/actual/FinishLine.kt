package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.BitmapDrawable
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.Updatable

class FinishLine(
    private val gameLogic: GameLogic,
    private val distance: Int,
    val endGame: () -> Unit,
    image: Bitmap
) :
    BitmapDrawable(image), Updatable {

    init {
        y = 0f
    }

    override fun tickUpdate(deltaTimeMillis: Long) {

        x = (distance - gameLogic.currentPos).toFloat()
    }

    override fun doIntersect(target: Intersectable): Boolean {
        val doIntersect = super.doIntersect(target)

        if (doIntersect) {
            endGame()
        }

        return doIntersect
    }
}
