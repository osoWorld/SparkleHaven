package com.example.sparklehaven

import android.app.Application
import com.google.firebase.FirebaseApp

class SparkleHavenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var instance: SparkleHavenApp
            private set
    }
}