package com.android.example.teseproject.BasesDados.Database_PP

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasswordUser::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
