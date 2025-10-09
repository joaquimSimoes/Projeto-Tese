package com.android.example.teseproject.BasesDados.Database_IP

import android.content.Context
import androidx.room.Room

object ImageDataBaseProvider {
    @Volatile
    private var INSTANCE: ImageRoomDatabase? = null

    fun getImageDatabase(context: Context): ImageRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                ImageRoomDatabase::class.java,
                "image_user_database"
            ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}