package com.example.bluetooth.database.models

import androidx.room.Entity

@Entity(tableName = "records", primaryKeys = ["recordName", "playerRecordName"])
data class Record(
    var recordName: String,
    val playerRecordName: String,
    val valuesList: List<Int>
)
