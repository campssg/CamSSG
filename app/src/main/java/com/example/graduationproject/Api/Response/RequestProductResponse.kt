package com.example.graduationproject.Api.Response

import com.google.gson.annotations.SerializedName

data class RequestProductResponse(
    val requestedProductId: Long,
    val requestedProductName: String,
    val requestedProductPrice: Int,
    val requestedProductCount: Int,
    @SerializedName("requestedProductImg")
    val requestedProductReference : String,
    val requestedProductState: String
)
