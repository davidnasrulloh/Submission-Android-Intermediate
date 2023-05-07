package com.davidnasrulloh.sm_intermediate_david.ui.splash

import androidx.lifecycle.ViewModel
import com.davidnasrulloh.sm_intermediate_david.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()
}