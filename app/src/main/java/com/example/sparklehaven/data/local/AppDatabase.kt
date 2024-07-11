package com.example.sparklehaven.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sparklehaven.data.model.Favorite

@Database(entities = [Favorite::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sparklehaven_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}