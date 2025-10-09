package com.android.example.teseproject.BasesDados.DataBase_RF

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FacialUserViewModel(private val facialDao: FacialDao) : ViewModel() {

    fun getUserByEmail(email: String, callback: (FacialUser?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                facialDao.getUserByEmail(email)
            }
            callback(user)
        }
    }

    fun insertUser(facialUser: FacialUser) {
        viewModelScope.launch(Dispatchers.IO) {
            facialDao.insert(facialUser)
        }
    }
    suspend fun getFacialByEmail(email: String): FacialUser?{
        return withContext(Dispatchers.IO){
            facialDao.getUserByEmail(email)
        }
    }
    suspend fun getFacialByMobile(mobile: String): FacialUser?{
        return withContext(Dispatchers.IO){
            facialDao.getUserByMobile(mobile)
        }
    }

}

class FacialUserViewModelFactory(private val facialDao: FacialDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FacialUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FacialUserViewModel(facialDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}