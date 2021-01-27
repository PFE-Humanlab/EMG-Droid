package com.example.bluetooth.game.objects.abstracs

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import com.example.bluetooth.game.objects.interf.Drawable
import com.example.bluetooth.game.objects.interf.GameObject
open class BitmapDrawable(private var image: Bitmap) : RectangleIntersectable(), Drawable,
    GameObject {

    override val wDraw: Int = image.width
    override val hDraw: Int = image.height
    override val wInter: Int = image.width
    override val hInter: Int = image.height

    val h: Int
        get() {
            return hDraw
        }
    val w: Int
        get() {
            return wDraw
        }

    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    override var xInter: Float = - w.toFloat()
    override var xDraw: Float = - w.toFloat()
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
        canvas.drawBitmap(image, xDraw, yDraw, null)
    }

    fun drawRotated(canvas: Canvas, rotationDegrees: Float = 0f) {

        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        val rotatedBitmap =
            Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        canvas.drawBitmap(rotatedBitmap, xDraw, yDraw, null)

    }

}

