package com.example.graduationproject.Api.Response

data class SearchMartItemResponse(
    val statusCode: String,
    val message: String,
    val data: SearchMartCategoryResponse
)

data class ProductList(
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val productStock: Int,
    val productImgUrl: String
)
