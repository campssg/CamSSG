package com.example.graduationproject.Api.Response


data class AddRequestProductResponse(
        val requestedProductCount: Int,
        val requestedProductId: Long,
        val requestedProductName: String,
        val requestedProductPrice: Int
)
