package com.example.bluetooth.utils

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