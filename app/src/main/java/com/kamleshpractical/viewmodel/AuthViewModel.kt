package com.kamleshpractical.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamleshpractical.navigation.AuthResult
import com.kamleshpractical.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, password: String) {
        if (email.isBlank()) {
            _errorMessage.value = "Email is required"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Enter valid email"
            return
        }

        if (password.isBlank()) {
            _errorMessage.value = "Password is required"
            return
        }
        if (password.length < 6) {
            _errorMessage.value = "Password should be at least 6 characters"
            return
        } else {
            Log.d("AuthViewModel", "login() called")
            viewModelScope.launch {
                repository.loginOrRegister(email, password).collect {
                    Log.d("AuthViewModel", "Auth result: $it")
                    _authState.value = it
                }
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
