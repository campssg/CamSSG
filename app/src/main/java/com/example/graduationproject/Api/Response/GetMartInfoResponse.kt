package com.example.graduationproject.Api.Response

data class GetMartInfoResponse(
    val statusCode: String,
    val message: String,
    val data: List<MartListResponse>
)
