package com.android.example.teseproject.BasesDados.Database_Otp

import android.content.Context
import androidx.room.Room

object OtpDataBaseProvider {
    @Volatile
    private var INSTANCE: OtpRoomDatabase? = null

    fun getOtpDatabase(context: Context): OtpRoomDatabase {
        return INSTANCE ?: synchronized(this){
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                OtpRoomDatabase::class.java,
                "otp_user_database"
            ).fallbackToDestructiveMigration()
                .build().also{ INSTANCE = it}
        }

    }
}