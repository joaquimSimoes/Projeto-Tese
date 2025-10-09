package com.android.example.teseproject.ViewModels

import android.content.Context
import com.android.example.teseproject.BasesDados.Database_FP.FingerDatabaseProvider
import com.android.example.teseproject.BasesDados.Database_IP.ImageDataBaseProvider
import com.android.example.teseproject.BasesDados.Database_Otp.OtpDataBaseProvider
import com.android.example.teseproject.BasesDados.Database_PP.PasswordDataBaseProvider
import com.android.example.teseproject.BasesDados.DataBase_RF.FacialDatabaseProvider

suspend fun clearUsersData(context: Context){
    val passwordDao = PasswordDataBaseProvider.getDatabase(context).userDao()
    val imageDao = ImageDataBaseProvider.getImageDatabase(context).imageDao()
    val fingerDao = FingerDatabaseProvider.getFingerDatabase(context).fingerDao()
    val facialDao = FacialDatabaseProvider.getFacialDatabase(context).facialDao()
    val otpDao = OtpDataBaseProvider.getOtpDatabase(context).otpDao()

    passwordDao.deleteAll()
    imageDao.deleteAll()
    fingerDao.deleteAll()
    facialDao.deleteAll()
    otpDao.deleteAll()
}