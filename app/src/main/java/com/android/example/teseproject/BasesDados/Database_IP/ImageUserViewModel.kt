package com.android.example.teseproject.BasesDados.Database_IP

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.example.teseproject.ExtraFunctions.hashImageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageUserViewModel(private val imageDao: ImageDao) : ViewModel() {

    fun getUserByEmail(email: String, callback: (ImageUser?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                imageDao.getUserByEmail(email)
            }
            callback(user)
        }
    }

    fun insertUser(imageUser: ImageUser) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDao.insert(imageUser)
        }
    }
    suspend fun getImageByEmail(email: String): ImageUser?{
        return withContext(Dispatchers.IO){
            imageDao.getUserByEmail(email)
        }
    }
    suspend fun getImageByMobile(mobile: String): ImageUser?{
        return withContext(Dispatchers.IO){
            imageDao.getUserByMobile(mobile)
        }
    }

    private val _imageLoginResult = mutableStateOf<Boolean?>(null)
    val imageLoginResult: State<Boolean?> = _imageLoginResult

    fun verifyImagePassword(email: String, selectedImages: List<Int>) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                imageDao.getUserByEmail(email)
            }
            val hashedImages = selectedImages.map{ hashImageId(it) }
            _imageLoginResult.value = user?.let {
                listOf(it.image1, it.image2).containsAll(hashedImages)
            } ?: false
        }
    }
}

class ImageUserViewModelFactory(private val imageDao: ImageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageUserViewModel(imageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
