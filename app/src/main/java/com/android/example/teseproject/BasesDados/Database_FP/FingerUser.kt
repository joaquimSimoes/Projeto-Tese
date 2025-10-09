package com.android.example.teseproject.BasesDados.Database_FP

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finger_users")
data class FingerUser (
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String
)