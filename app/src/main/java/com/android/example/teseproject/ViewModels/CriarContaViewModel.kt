package com.android.example.teseproject.ViewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CriarContaViewModel : ViewModel() {
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _mobile = MutableStateFlow("")
    val mobile: StateFlow<String> = _mobile

    fun putFirstName(value: String){
        _firstName.value = value
    }

    fun putLastName(value: String){
        _lastName.value = value
    }

    fun putEmail(value: String){
        _email.value = value
    }

    fun putMobile(value: String){
        _mobile.value = value
    }

    fun clearAllFields() {
        _firstName.value = ""
        _lastName.value = ""
        _email.value = ""
        _mobile.value = ""
    }
}