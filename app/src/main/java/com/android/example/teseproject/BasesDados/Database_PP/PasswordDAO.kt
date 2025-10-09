package com.android.example.teseproject.BasesDados.Database_PP

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(passwordUser: PasswordUser)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): PasswordUser?

    @Query("SELECT * FROM users WHERE mobile = :mobile")
    suspend fun getUserByMobile(mobile: String): PasswordUser?

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
