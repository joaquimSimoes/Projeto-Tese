package com.android.example.teseproject.BasesDados.Database_IP

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_users")
data class ImageUser(
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val image1: String,
    val image2: String
)
