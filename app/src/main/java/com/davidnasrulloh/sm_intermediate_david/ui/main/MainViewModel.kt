package com.davidnasrulloh.sm_intermediate_david.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.davidnasrulloh.sm_intermediate_david.data.AuthRepository
import com.davidnasrulloh.sm_intermediate_david.data.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
    fun saveAuthToken(token: String){
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}