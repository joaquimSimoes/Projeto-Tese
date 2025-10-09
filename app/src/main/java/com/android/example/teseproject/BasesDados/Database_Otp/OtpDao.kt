package com.android.example.teseproject.BasesDados.Database_Otp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface OtpDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: OtpUser)

    @Query("SELECT * FROM otp_users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): OtpUser?

    @Query("DELETE FROM otp_users")
    suspend fun deleteAll()

    @Query("SELECT * FROM otp_users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): OtpUser?
}