package com.example.bluetooth.database.converter

import androidx.room.TypeConverter

class Converter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromListInt(value: List<Int>): String {
            return value.joinToString(",")
        }

        @TypeConverter
        @JvmStatic
        fun toListInt(value: String): List<Int> {
            return value.split(",").map { Integer.parseInt(it) }
        }
    }
}