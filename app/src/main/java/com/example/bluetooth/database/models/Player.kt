package com.example.bluetooth.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(@PrimaryKey var name: String) {
    //var recordList : List<Record> = listOf()
    var maxLevel : Int = 1
}