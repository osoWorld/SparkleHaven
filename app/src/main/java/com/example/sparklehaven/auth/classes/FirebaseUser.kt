package com.example.sparklehaven.auth.classes

data class FirebaseUser(
    val username : String = "",
    val email : String = "",
    val password : String = "",
    val uid : String = "",
    val userImageUrl : String = "",
    val age : String = ""
)
