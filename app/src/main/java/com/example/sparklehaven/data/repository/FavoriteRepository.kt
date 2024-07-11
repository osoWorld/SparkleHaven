package com.example.sparklehaven.data.repository

import com.example.sparklehaven.Product
import com.example.sparklehaven.data.local.FavoriteDao
import com.example.sparklehaven.data.model.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FavoriteRepository(private val favoriteDao: FavoriteDao, private val db: FirebaseFirestore) {

    suspend fun toggleFavorite(userId: String, product: Product) {
        val favoriteRef = db.collection("favorites")
            .whereEqualTo("userId", userId)
            .whereEqualTo("productId", product.productId)

        val documents = favoriteRef.get().await()
        if (documents.isEmpty) {
            val favorite = Favorite(userId, product.productId)
            db.collection("favorites").add(favorite).await()
            favoriteDao.insertFavorite(favorite)
        } else {
            for (document in documents) {
                document.reference.delete().await()
            }
            val favorite = Favorite(userId, product.productId)
            favoriteDao.deleteFavorite(favorite)
        }
    }

    suspend fun fetchFavorites(userId: String): List<Favorite> {
        val firebaseFavorites = db.collection("favorites")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(Favorite::class.java)

        favoriteDao.insertFavorites(firebaseFavorites)
        return favoriteDao.getFavoritesForUser(userId)
    }

    suspend fun fetchProductsByIds(productIds: List<String>): List<Product> {
        val products = mutableListOf<Product>()
        for (productId in productIds) {
            val productSnapshot = db.collection("products").document(productId).get().await()
            productSnapshot.toObject(Product::class.java)?.let { products.add(it) }
        }
        return products
    }
}