package com.example.graduationproject.Api.Request

data class CampWishRequest(
    val campAddress: String,
    val campName: String,
    val campTel: String,
    val latitude: Double,
    val longitude: Double
)
