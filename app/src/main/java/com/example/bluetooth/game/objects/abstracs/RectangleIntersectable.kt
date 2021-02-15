package com.example.bluetooth.game.objects.abstracs

import android.graphics.Rect
import com.example.bluetooth.game.objects.interf.Intersectable

abstract class RectangleIntersectable : Intersectable {

    abstract val wInter: Int
    abstract val hInter: Int

    override fun doIntersect(target: Intersectable): Boolean {

        if (target is RectangleIntersectable) {

            val sourceRect = Rect(
                xInter.toInt(),
                yInter.toInt(),
                (xInter + wInter).toInt(),
                (yInter + hInter).toInt()
            )

            return sourceRect.intersects(
                target.xInter.toInt(),
                target.yInter.toInt(),
                (target.xInter + target.wInter).toInt(),
                (target.yInter + target.hInter).toInt()
            )
        } else {
            return false
        }
    }
}
