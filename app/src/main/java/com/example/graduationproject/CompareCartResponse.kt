package com.example.graduationproject

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CompareCartResponse(
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data?
)

data class Data(
    @SerializedName("cartComparisonList")
    val cartComparisonList: List<CartComparisonItem>
)

data class CartComparisonItem(
    @SerializedName("martImg")
    val martImg: String,
    @SerializedName("martName")
    val martName: String,
    @SerializedName("notExistsCnt")
    val notExistCnt: Int,
    @SerializedName("totalPrice")
    val totalPrice: Int
)