package com.android.example.teseproject.BasesDados.Database_Otp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OtpUser::class], version = 2)
abstract class OtpRoomDatabase : RoomDatabase(){
    abstract fun otpDao(): OtpDao
}