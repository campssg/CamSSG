package com.example.graduationproject.Api.Response

import java.time.LocalDateTime

data class UserOrderListResponse(
    val martName: String,
    val orderId: Long,
    val orderState: String,
    val reservedAt: LocalDateTime,
    val totalPrice: Int
)
