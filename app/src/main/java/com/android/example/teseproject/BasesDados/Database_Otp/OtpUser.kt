package com.android.example.teseproject.BasesDados.Database_Otp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "otp_users")
data class OtpUser(
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String
)
