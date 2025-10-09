package com.android.example.teseproject.BasesDados.DataBase_RF

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FacialDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: FacialUser)

    @Query("SELECT * FROM facial_users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): FacialUser?

    @Query("SELECT * FROM facial_users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): FacialUser?

    @Query("DELETE FROM facial_users")
    suspend fun deleteAll()
}