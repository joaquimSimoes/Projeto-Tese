package com.android.example.teseproject.BasesDados.DataBase_All

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allMethods")
data class All_Methods_Data(
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val mobile: String,

    // Authentication method flags
    val hasPassword: Boolean = false,
    val hasImagePassword: Boolean = false,
    val hasFingerprint: Boolean = false,
    val hasFacial: Boolean = false,
    val hasOtp: Boolean = false,

    // Actual data for each method (nullable until chosen)
    val passwordHash: String? = null,
    val image1: String? = null,
    val image2: String? = null,
    val fingerprintTemplate: String? = null,
    val facialTemplate: String? = null,
    val otpSecret: String? = null,
)
