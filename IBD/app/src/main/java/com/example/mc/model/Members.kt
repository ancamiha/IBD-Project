package com.example.mc.model

data class Members(
    val members: List<Member>
)

data class Member(
    var address: String,
    var email: String,
    var lat: Double,
    var lng: Double,
    var name: String,
    var timestamp: String
)