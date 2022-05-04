package com.example.graduationproject.Api.Request

data class MartEditRequest(
    val martId: Long,
    val martName: String,
    val openTime: String,
    val closeTime: String,
    val requestYn: Long
)
