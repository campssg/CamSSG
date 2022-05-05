package com.example.graduationproject.Api.Request

data class AddRequestProductRequest(
        val orderId : Long,
        val requestedProductCount: Int,
        val requestedProductName: String,
        val requestedProductPrice: Int,
        val requestedProductReference: String
)
