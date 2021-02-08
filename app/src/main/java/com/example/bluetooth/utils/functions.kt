package com.example.bluetooth.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import java.text.SimpleDateFormat
import java.util.*

fun String.leftPad(wantedLength: Int, character: String = " "): String {
    var i = 0

    val length = wantedLength - this.length
    val builder = StringBuilder()

    while (i < length) {
        builder.append(character)
        i++
    }

    builder.append(this)

    return builder.toString()
}

fun Bitmap.rotatedBitmap(rotationDegrees: Float): Bitmap {

    val matrix = Matrix().apply { postRotate(rotationDegrees) }
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.resizedBitmap(newHeight: Int): Bitmap {

    val oldWidth = this.width
    val oldHeight = this.height

    val scaleFactor = newHeight.toFloat() / oldHeight

    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleFactor, scaleFactor)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(
        this, 0, 0, oldWidth, oldHeight, matrix, false
    )

    return resizedBitmap
}

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.FRENCH)
    return format.format(this)
}

fun Float.uniformTransform(minInt: Float, maxInit: Float, minFinal: Float, maxFinal: Float) : Float{
    // this € [min , Max]

    // [min , Max] - min => [0, max - min]
    val centered = this - minInt
    // [0, max - min] / (max - min) => [0, 1]
    val reduced = centered / (maxInit - minInt)
    // [0, 1] * (maxF - minF) => [0, maxF - minF]
    val sized = reduced * (maxFinal - minFinal)

    // [0, maxF - minF] + minF => [minF, maxF]
    return sized + minFinal
}