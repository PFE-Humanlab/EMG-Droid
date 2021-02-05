package com.example.bluetooth.database.models.jointure

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bluetooth.database.models.Player
import com.example.bluetooth.database.models.Record

class PlayerWithRecord(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "playerName",
        entityColumn = "playerRecordName"
    )
    val records: List<Record>
)