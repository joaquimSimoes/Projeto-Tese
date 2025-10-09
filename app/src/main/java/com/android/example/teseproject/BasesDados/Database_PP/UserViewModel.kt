package com.android.example.teseproject.BasesDados.Database_PP

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    fun getUserByEmail(email: String, callback: (PasswordUser?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.getUserByEmail(email)
            }
            callback(user)
        }
    }
    fun insertUser(passwordUser: PasswordUser) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(passwordUser)
        }
    }
    suspend fun getPasswordByEmail(email: String): PasswordUser?{
        return withContext(Dispatchers.IO){
            userDao.getUserByEmail(email)
        }
    }
    suspend fun getPasswordByMobile(mobile: String): PasswordUser?{
        return withContext(Dispatchers.IO){
            userDao.getUserByMobile(mobile)
        }
    }

}
class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
