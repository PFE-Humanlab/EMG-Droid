package com.example.bluetooth.game.objects.example

import android.graphics.Bitmap
import com.example.bluetooth.game.objects.abstracs.Intersectable
import com.example.bluetooth.game.objects.abstracs.PlayerUpdatable

class Player(image: Bitmap) : Intersectable(image), PlayerUpdatable {

    override fun playerUpdate(touch_x: Int, touch_y: Int) {
        x = touch_x - w / 2f
        y = touch_y - h / 2f
    }

}