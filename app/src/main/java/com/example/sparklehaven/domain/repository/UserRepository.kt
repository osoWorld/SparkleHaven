package com.example.sparklehaven.domain.repository

import com.example.sparklehaven.data.model.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
}