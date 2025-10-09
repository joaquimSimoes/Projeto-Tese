package com.android.example.teseproject.BasesDados.Database_IP

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageUser::class], version = 2)
abstract class ImageRoomDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}