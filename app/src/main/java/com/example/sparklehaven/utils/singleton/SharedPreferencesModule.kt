package com.example.sparklehaven.utils.singleton

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesModule {
    private const val PREF_NAME = "sparklehaven_prefs"

    @Volatile
    private var INSTANCE: SharedPreferences? = null

    fun getSharedPreferences(context: Context): SharedPreferences {
        return INSTANCE ?: synchronized(this) {
            val instance = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            INSTANCE = instance
            instance
        }
    }
}