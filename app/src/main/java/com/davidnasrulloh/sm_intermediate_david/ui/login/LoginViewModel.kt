package com.davidnasrulloh.sm_intermediate_david.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.sm_intermediate_david.data.remote.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    suspend fun userLogin(email: String, password: String) =
        authRepository.userLogin(email, password)

    fun saveAuthToken(token: String){
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}