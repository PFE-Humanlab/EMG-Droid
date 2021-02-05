package com.example.bluetooth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bluetooth.database.models.Record

@Dao
interface RecordDAO {

    @Insert
    suspend fun insertAll(vararg records: Record)

    @Query("SELECT * FROM records WHERE playerRecordName = :name")
    suspend fun getRecordsByPlayerName(name: String): List<Record>

    @Query("SELECT * FROM records WHERE playerRecordName = :playerName and recordName = :recordName")
    suspend fun getRecord(recordName: String, playerName: String): Record
}