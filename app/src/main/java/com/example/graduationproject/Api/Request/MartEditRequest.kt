package com.example.graduationproject.Api.Request

data class MartEditRequest(
    val martId: Long,
    val martName: String,
    val openTiem: String,
    val closeTiem: String,
    val requestYn: Long
)
