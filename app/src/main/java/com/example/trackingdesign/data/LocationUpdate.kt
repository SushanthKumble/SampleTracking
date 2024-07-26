package com.example.trackingdesign.data

data class LocationUpdate(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
