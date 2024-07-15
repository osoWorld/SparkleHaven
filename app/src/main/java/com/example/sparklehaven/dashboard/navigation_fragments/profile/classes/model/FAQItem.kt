package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model

data class FAQItem (
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)