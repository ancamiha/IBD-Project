package com.example.mc.model

data class Markers(
    val markers: List<Marker>
)

data class Marker(
    val email: String,
    val lat: Double,
    val lng: Double,
    val name: String,
    val time: String
)
