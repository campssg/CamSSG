package com.example.graduationproject.Api.Response

data class MartListResponse(
    val martId: Long,
    val martName: String,
    val martAddress: String,
    val openTime: String,
    val closeTime: String,
    val latitude: Double,
    val longitude: Double,
    val requestYn: Long,
    val distance : Double,
    val martImg: String
)
