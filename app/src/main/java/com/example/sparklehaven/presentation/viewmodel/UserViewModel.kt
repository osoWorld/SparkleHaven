package com.example.sparklehaven.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparklehaven.domain.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getUser(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(userId)
            // Handle user data
        }
    }
}