package com.example.bluetooth.utils

import android.graphics.Bitmap
import android.graphics.Matrix

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