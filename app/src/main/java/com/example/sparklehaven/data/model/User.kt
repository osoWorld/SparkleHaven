package com.example.sparklehaven.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = ""

    // Add other fields as necessary
)
