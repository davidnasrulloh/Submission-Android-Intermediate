package com.davidnasrulloh.sm_intermediate_david.ui.register

import androidx.lifecycle.ViewModel
import com.davidnasrulloh.sm_intermediate_david.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    suspend fun userRegister(name:String, email: String, password: String) =
        authRepository.userRegister(name, email, password)
}