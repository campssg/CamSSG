package com.example.graduationproject.Api.Response

data class SearchMartItemResponse(
    val statusCode: String,
    val message: String,
    val data: Data
)
data class Data(
    val categoryName: String,
    val productList: List<ProductList>
)

data class ProductList(
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val productStock: Int
)
