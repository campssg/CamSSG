package com.example.graduationproject.Api.Response

import java.time.LocalDateTime

data class UserOrderListResponse(
    val martName: String,
    val orderId: Long,
    val orderState: String,
    val pickup_day: String,
    val pickup_time: String,
    val totalPrice: Int
)
