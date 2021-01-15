package com.example.bluetooth.game.objects.example

import android.graphics.Bitmap
import com.example.bluetooth.game.objects.abstracs.Intersectable
import com.example.bluetooth.game.objects.abstracs.Updatable

class MovingObstacle(image: Bitmap) : Intersectable(image), Updatable {

    private var velocity = 500

    private var xDir = 1
    private var yDir = 1

    init {
        x = screenWidth / 2f
        y = screenHeight / 2f
    }

    override fun tickUpdate(deltaTimeMillis: Long) {

        if (x + w > screenWidth) {
            xDir = -1
        } else if (x < 0) {
            xDir = 1
        }

        if (y + h > screenHeight) {
            yDir = -1
        } else if (y < 0) {
            yDir = 1
        }

        val xDeltaVelocity = xDir * velocity * deltaTimeMillis / 1000f
        val yDeltaVelocity = yDir * velocity * deltaTimeMillis / 1000f

        x += xDeltaVelocity
        y += yDeltaVelocity

    }

    override fun intersect(target: Intersectable) {
        if (xDir == 1) { // left to right
            if (yDir == 1) { // top to bottom

                if(x + w > target.x && x < target.x){ // hit left border of target
                    xDir = -1
                }
                if(y + h > target.y && y < target.y){ // hit top border of target
                    yDir = -1
                }

            } else {// bottom to top
                if(x + w > target.x && x < target.x){ // hit left border of target
                    xDir = -1
                }
                if(y + h > target.y + target.h && y < target.y + target.h){ // hit bottom border of target
                    yDir = 1
                }
            }
        } else {// right to left
            if (yDir == 1) { // top to bottom
                if(x + w > target.x + target.w && x < target.x + target.w){ // hit right border of target
                    xDir = 1
                }
                if(y + h > target.y && y < target.y){ // hit top border of target
                    yDir = -1
                }
            } else {// bottom to top
                if(x + w > target.x + target.w && x < target.x + target.w){ // hit right border of target
                    xDir = 1
                }
                if(y + h > target.y + target.h && y < target.y + target.h){ // hit bottom border of target
                    yDir = 1
                }
            }
        }
    }
}