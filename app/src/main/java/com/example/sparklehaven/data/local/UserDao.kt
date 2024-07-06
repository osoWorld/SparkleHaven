package com.example.sparklehaven.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sparklehaven.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId: String): User?
}