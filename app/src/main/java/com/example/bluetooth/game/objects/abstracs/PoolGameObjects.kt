package com.example.bluetooth.game.objects.abstracs

import android.graphics.Canvas
import com.example.bluetooth.game.objects.interf.GameObject
import com.example.bluetooth.game.objects.interf.Intersectable
import com.example.bluetooth.game.objects.interf.Updatable

abstract class PoolGameObjects(private val list: List<GameObject>) : GameObject {

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
        return list.filter { it.doIntersect(target) }.isNotEmpty()
    }


}