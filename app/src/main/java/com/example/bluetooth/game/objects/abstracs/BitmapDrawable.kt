package com.example.bluetooth.game.objects.abstracs

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.bluetooth.game.objects.interf.Drawable
import com.example.bluetooth.game.objects.interf.GameObject
import com.example.bluetooth.utils.rotatedBitmap

open class BitmapDrawable(private val image: Bitmap) :
    RectangleIntersectable(), Drawable, GameObject {

    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    override val wDraw: Int
        get() {
            return image.width
        }
    override val hDraw: Int
        get() {
            return image.height
        }
    override val wInter: Int
        get() {
            return image.width
        }
    override val hInter: Int
        get() {
            return image.height
        }

    val h: Int
        get() {
            return hDraw
        }
    val w: Int
        get() {
            return wDraw
        }


    override var xInter: Float = screenWidth.toFloat()
    override var xDraw: Float = screenWidth.toFloat()
    var x: Float
        get() = xDraw
        set(value) {
            xDraw = value
            xInter = value
        }

    override var yInter: Float = screenHeight.toFloat()
    override var yDraw: Float = screenHeight.toFloat()

    var y: Float
        get() = yDraw
        set(value) {
            yDraw = value
            yInter = value
        }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x, y, null)
    }

    fun drawRotated(canvas: Canvas, rotationDegrees: Float = 0f) {
        canvas.drawBitmap(image.rotatedBitmap(rotationDegrees), x, y, null)
    }

}

