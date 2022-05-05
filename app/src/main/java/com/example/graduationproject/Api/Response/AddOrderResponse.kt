package com.example.graduationproject.Api.Response

data class AddOrderResponse(
    val orderId: String,
    val martName: String,
    val pickup_day: String,
    val pickup_time: String,
    val userName: String,
    val order_phoneNumber: String,
    val orderState: String,
    val requestYn: String,
    val totalPrice: String
)
