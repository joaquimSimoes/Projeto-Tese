package com.android.example.teseproject.BasesDados.DataBase_RF

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facial_users")
data class FacialUser (
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String
)