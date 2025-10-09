package com.android.example.teseproject.BasesDados.Database_FP

import android.content.Context
import androidx.room.Room

object FingerDatabaseProvider {
    @Volatile
    private var INSTANCE: FingerRoomDatabase? = null

    fun getFingerDatabase(context: Context): FingerRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                FingerRoomDatabase::class.java,
                "finger_user_database"
            ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}