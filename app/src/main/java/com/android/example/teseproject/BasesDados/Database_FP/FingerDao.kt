package com.android.example.teseproject.BasesDados.Database_FP

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FingerDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: FingerUser)

    @Query("SELECT * FROM finger_users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): FingerUser?

    @Query("SELECT * FROM finger_users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): FingerUser?

    @Query("DELETE FROM finger_users")
    suspend fun deleteAll()
}