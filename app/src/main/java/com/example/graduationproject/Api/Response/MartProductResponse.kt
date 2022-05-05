package com.example.graduationproject.Api.Response

// 마트 상품 조회 response
data class MartProductResponse(
        val statusCode: String,
        val message: String,
        val data: MartCategoryResponse
)

data class ProductList2(
        val productId: Long,
        val productName: String,
        val productPrice: Int,
        val productStock: Int,
        val productImgUrl: String
)