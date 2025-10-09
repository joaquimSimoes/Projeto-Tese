package com.android.example.teseproject.BasesDados.DataBase_All

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface All_Methods_Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(allMethods: All_Methods_Data)

    @Update
    suspend fun update(allMethods: All_Methods_Data)

    @Query("SELECT * FROM allMethods WHERE email = :email")
    suspend fun getMethodByEmail(email: String): All_Methods_Data?

    @Query("SELECT * FROM allMethods WHERE mobile = :mobile")
    suspend fun getMethodByMobile(mobile: String): All_Methods_Data?

    @Query("DELETE FROM allMethods")
    suspend fun deleteAll()

    @Query("SELECT * FROM allMethods")
    suspend fun getAllUsers(): List<All_Methods_Data>
}