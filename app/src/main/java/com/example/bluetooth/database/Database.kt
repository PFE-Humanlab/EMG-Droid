package com.example.bluetooth.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bluetooth.database.dao.PlayerDao
import com.example.bluetooth.database.models.Player
import java.security.AccessControlContext

@Database(entities = arrayOf(Player::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context): AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "roomdb").fallbackToDestructiveMigration()
                        .build()
            }

            return INSTANCE as AppDatabase
        }
    }
    abstract fun playerDao(): PlayerDao
}