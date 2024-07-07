package com.example.sparklehaven.utils.singleton

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseModule {
    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firebaseFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val firebaseStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
}