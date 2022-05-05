package com.example.graduationproject.Api.Response

data class MartOrderListResponse(
    val userName: String,
    val orderId: Long,
    val orderState: String,
    val order_phoneNumber: String,
    val pickup_day: String,
    val pickup_time: String,
    val totalPrice: Int
)
