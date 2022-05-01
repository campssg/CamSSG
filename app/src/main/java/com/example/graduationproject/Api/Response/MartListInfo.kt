package com.example.graduationproject.Api.Response

data class MartListInfo(
    val statusCode: String,
    val message: String,
    val data: List<MartListResponse>
)
