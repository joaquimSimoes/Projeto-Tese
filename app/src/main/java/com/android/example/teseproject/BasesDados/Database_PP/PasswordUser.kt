package com.android.example.teseproject.BasesDados.Database_PP

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class PasswordUser(
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val hashedPassword: String?,
    )
