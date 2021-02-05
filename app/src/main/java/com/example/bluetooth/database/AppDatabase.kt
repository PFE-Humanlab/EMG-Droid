package com.example.bluetooth.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bluetooth.database.converter.Converter
import com.example.bluetooth.database.dao.LevelDAO
import com.example.bluetooth.database.dao.PlayerDao
import com.example.bluetooth.database.dao.RecordDAO
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level
import com.example.bluetooth.database.models.Player
import com.example.bluetooth.database.models.Record

@Database(entities = [Player::class, Level::class, Record::class, BestScore::class], version = 3)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "roomdb"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun playerDao(): PlayerDao
    abstract fun levelDao(): LevelDAO
    abstract fun recordDAO(): RecordDAO
}