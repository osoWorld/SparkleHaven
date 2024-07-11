package com.example.sparklehaven.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val userId: String = "",
    val productId: String = "",
    val isFavorite: Boolean = false
)
