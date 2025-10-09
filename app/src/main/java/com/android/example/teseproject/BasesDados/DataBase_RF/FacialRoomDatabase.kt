package com.android.example.teseproject.BasesDados.DataBase_RF

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FacialUser::class], version = 2)
abstract class FacialRoomDatabase : RoomDatabase() {
    abstract fun facialDao(): FacialDao
}