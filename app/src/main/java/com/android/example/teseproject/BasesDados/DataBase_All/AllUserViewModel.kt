package com.android.example.teseproject.BasesDados.DataBase_All

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllUserViewModel(private val allDao: All_Methods_Dao) : ViewModel(){
    suspend fun getAllUserByEmail(email: String): All_Methods_Data?{
        return allDao.getMethodByEmail(email)

    }
    suspend fun insertUser(allUser: All_Methods_Data){

            allDao.insertMethod(allUser)

    }
    suspend fun getAllUserByMobile(mobile: String): All_Methods_Data?{
        return allDao.getMethodByMobile(mobile)

    }

    suspend fun updateUser(allUser: All_Methods_Data) {
            allDao.update(allUser)
    }

    suspend fun getAllUsers(): List<All_Methods_Data> {
        return allDao.getAllUsers()
    }


    suspend fun isPasswordUsed(email: String, mobile: String): Pair<Boolean, String?> {
        val user = getAllUserByEmail(email)
        val mobileUser = getAllUserByMobile(mobile)

        return when {
            user?.hasPassword == true -> true to "email"
            mobileUser?.hasPassword == true -> true to "mobile"
            else -> false to null
        }
    }
    suspend fun isImagePasswordUsed(email: String, mobile: String): Pair<Boolean, String?> {
        val user = getAllUserByEmail(email)
        val mobileUser = getAllUserByMobile(mobile)

        return when {
            user?.hasImagePassword == true -> true to "email"
            mobileUser?.hasImagePassword == true -> true to "mobile"
            else -> false to null
        }
    }
    suspend fun isOTPUsed(email: String, mobile: String): Pair<Boolean, String?> {
        val user = getAllUserByEmail(email)
        val mobileUser = getAllUserByMobile(mobile)

        return when {
            user?.hasOtp == true -> true to "email"
            mobileUser?.hasOtp == true -> true to "mobile"
            else -> false to null
        }
    }
    suspend fun isFingerPrintUsed(email: String, mobile: String): Pair<Boolean, String?> {
        val user = getAllUserByEmail(email)
        val mobileUser = getAllUserByMobile(mobile)

        return when {
            user?.hasFingerprint == true -> true to "email"
            mobileUser?.hasFingerprint == true -> true to "mobile"
            else -> false to null
        }
    }
    suspend fun isFacialUsed(email: String, mobile: String): Pair<Boolean, String?> {
        val user = getAllUserByEmail(email)
        val mobileUser = getAllUserByMobile(mobile)

        return when {
            user?.hasFacial == true -> true to "email"
            mobileUser?.hasFacial == true -> true to "mobile"
            else -> false to null
        }
    }
}

class AllUserViewModelFactory(private val allDao: All_Methods_Dao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(AllUserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AllUserViewModel(allDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}