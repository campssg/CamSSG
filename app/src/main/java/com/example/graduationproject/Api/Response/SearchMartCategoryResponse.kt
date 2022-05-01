package com.example.graduationproject.Api.Response

data class SearchMartCategoryResponse(
    val categoryName: String,
    val productList: List<ProductList>
)
