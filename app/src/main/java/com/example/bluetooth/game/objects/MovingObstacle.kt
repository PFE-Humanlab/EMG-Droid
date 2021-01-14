package com.example.bluetooth.game.objects

import android.graphics.Bitmap
import android.graphics.Canvas

class MovingObstacle(image: Bitmap) : GameObject(image) {

    var x: Int = 0
    var y: Int = 0

    var xVelocity = 10
    var yVelocity = 10

    init {
        x = screenWidth / 2
        y = screenHeight / 2
    }

    /**
     * Draws the object on to the canvas.
     */
    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    /**
     * update properties for the game object
     */
    override fun update() {

        if (x > screenWidth - image.width || x < image.width) {
            xVelocity = xVelocity * -1
        }
        if (y > screenHeight - image.height || y < image.height) {
            yVelocity = yVelocity * -1
        }

        x += (xVelocity)
        y += (yVelocity)

    }
}