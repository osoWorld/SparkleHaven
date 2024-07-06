package com.example.sparklehaven.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.example.sparklehaven.data.model.User
import kotlinx.coroutines.tasks.await

class FirebaseService (private val firestore: FirebaseFirestore) {
    suspend fun getUser(userId: String): User? {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}