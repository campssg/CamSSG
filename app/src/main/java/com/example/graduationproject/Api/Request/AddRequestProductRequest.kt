package com.example.graduationproject.Api.Request

data class AddRequestProductRequest(
        val orderId : String,
        val requestedProductCount: Int,
        val requestedProductName: String,
        val requestedProductPrice: Int,
        val requestedProductReference: String
)
