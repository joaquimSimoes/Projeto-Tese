package com.android.example.teseproject.BasesDados.Database_FP

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FingerUser::class], version = 2)
abstract class FingerRoomDatabase : RoomDatabase() {
    abstract fun fingerDao(): FingerDao
}