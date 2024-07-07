package com.example.sparklehaven.utils.singleton

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.local.UserDao
import javax.inject.Singleton

object RoomModule {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "sparklehaven_db").build()
    }

    fun provideExampleDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}