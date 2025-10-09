package com.android.example.teseproject.BasesDados.Database_FP

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FingerUserViewModel(private val fingerDao: FingerDao) : ViewModel() {

    fun getUserByEmail(email: String, callback: (FingerUser?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                fingerDao.getUserByEmail(email)
            }
            callback(user)
        }
    }
    fun insertUser(fingerUser: FingerUser) {
        viewModelScope.launch(Dispatchers.IO) {
            fingerDao.insert(fingerUser)
        }
    }

    suspend fun getFingerByEmail(email: String): FingerUser?{
        return withContext(Dispatchers.IO){
            fingerDao.getUserByEmail(email)
        }
    }
    suspend fun getFingerByMobile(mobile: String): FingerUser?{
        return withContext(Dispatchers.IO){
            fingerDao.getUserByMobile(mobile)
        }
    }

}
class FingerUserViewModelFactory(private val fingerDao: FingerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FingerUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FingerUserViewModel(fingerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}