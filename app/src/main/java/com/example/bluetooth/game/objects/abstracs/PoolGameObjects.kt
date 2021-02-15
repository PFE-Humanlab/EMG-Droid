package com.example.bluetooth.game.objects.abstracs

import android.graphics.Canvas
import com.example.bluetooth.game.objects.interf.GameObject
import com.example.bluetooth.game.objects.interf.Intersectable

abstract class PoolGameObjects<T : GameObject> : GameObject {

    abstract val list: MutableList<T>

    override val wDraw: Int = 0
    override val hDraw: Int = 0
    override var xInter: Float = 0f
    override var yInter: Float = 0f

    override fun draw(canvas: Canvas) {
        list.forEach {
            it.draw(canvas)
        }
    }

    override fun doIntersect(target: Intersectable): Boolean {
        val intersectedList = list.filter { it.doIntersect(target) }
        return intersectedList.isNotEmpty()
    }
}
