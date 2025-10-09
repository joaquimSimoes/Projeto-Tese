package com.android.example.teseproject.BasesDados.Database_PP

import android.content.Context
import androidx.room.Room

object PasswordDataBaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "user-database"
            ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}


//para uso fora: val db = DatabaseProvider.getDatabase(context)