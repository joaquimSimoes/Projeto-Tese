package com.android.example.teseproject.BasesDados.DataBase_RF

import android.content.Context
import androidx.room.Room

object FacialDatabaseProvider {
    @Volatile
    private var INSTANCE: FacialRoomDatabase? = null

    fun getFacialDatabase(context: Context): FacialRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                FacialRoomDatabase::class.java,
                "facial_user_database"
            ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}