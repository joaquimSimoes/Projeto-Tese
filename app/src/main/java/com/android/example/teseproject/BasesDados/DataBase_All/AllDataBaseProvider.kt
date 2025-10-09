package com.android.example.teseproject.BasesDados.DataBase_All

import android.content.Context
import androidx.room.Room

object AllDataBaseProvider {
    @Volatile
    private var INSTANCE: AllRoomDatabase? = null

    fun getDatabase(context: Context): AllRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AllRoomDatabase::class.java,
                "all-database"
            ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}


//para uso fora: val db = DatabaseProvider.getDatabase(context)