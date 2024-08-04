package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model

import com.google.firebase.firestore.PropertyName

data class Message(
    @PropertyName("senderId") val senderId: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("timestamp") val timestamp: Long = 0,
    @PropertyName("replyTo") val replyTo: String? = null
)
