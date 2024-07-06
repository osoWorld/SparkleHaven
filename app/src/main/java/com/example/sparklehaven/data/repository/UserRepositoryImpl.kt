package com.example.sparklehaven.data.repository

import com.example.sparklehaven.data.local.UserDao
import com.example.sparklehaven.data.model.User
import com.example.sparklehaven.data.remote.FirebaseService
import com.example.sparklehaven.domain.repository.UserRepository

class UserRepositoryImpl (private val userDao: UserDao, private val firebaseService: FirebaseService) :
    UserRepository {
    override suspend fun getUser(userId: String): User? {
        var user = userDao.getUser(userId)
        if (user == null) {
            user = firebaseService.getUser(userId)
            user?.let { userDao.insertUser(it) }
        }
        return user
    }
}