package com.android.example.teseproject.BasesDados.Database_IP

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: ImageUser)

    @Query("SELECT * FROM image_users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): ImageUser?

    @Query("SELECT * FROM image_users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): ImageUser?

    @Query("DELETE FROM image_users")
    suspend fun deleteAll()
}