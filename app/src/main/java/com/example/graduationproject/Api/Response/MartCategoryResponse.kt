package com.example.graduationproject.Api.Response

// 마트 상품 카테고리 조회 response
data class MartCategoryResponse(
        val categoryName: String,
        val productList: List<ProductList2>
)