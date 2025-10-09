package com.android.example.teseproject.BasesDados.Database_Otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpUserViewModel(private val otpDao: OtpDao) : ViewModel(){
    suspend fun getOtpByEmail(email: String): OtpUser?{
        return withContext(Dispatchers.IO){
            otpDao.getUserByEmail(email)
        }
    }
    fun insertUser(otpUser: OtpUser){
        viewModelScope.launch(Dispatchers.IO) {
            otpDao.insert(otpUser)
        }
    }
    suspend fun getOtpByMobile(mobile: String): OtpUser?{
        return withContext(Dispatchers.IO){
            otpDao.getUserByMobile(mobile)
        }
    }
}

class OtpUserViewModelFactory(private val otpDao: OtpDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(OtpUserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OtpUserViewModel(otpDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}