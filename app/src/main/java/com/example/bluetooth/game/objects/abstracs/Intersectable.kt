package com.example.bluetooth.game.objects.abstracs

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.bluetooth.game.view.GameView

@Suppress("SpellCheckingInspection")
abstract class Intersectable(image: Bitmap) : Drawable(image) {

    init {
        GameView.listIntersectable.add(this)
    }

    fun doIntersect(target: Intersectable): Boolean {

        val sourceRect = Rect(x.toInt(), y.toInt(), (x + w).toInt(), (y + h).toInt())

        return sourceRect.intersects(
            target.x.toInt(),
            target.y.toInt(),
            (target.x + target.w).toInt(),
            (target.y + target.h).toInt()
        )
    }

    open fun intersect(target: Intersectable) {

    }

}