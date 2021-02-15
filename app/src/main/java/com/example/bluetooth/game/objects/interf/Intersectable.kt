package com.example.bluetooth.game.objects.interf

interface Intersectable {

    var xInter: Float
    var yInter: Float

    fun doIntersect(target: Intersectable): Boolean
}
