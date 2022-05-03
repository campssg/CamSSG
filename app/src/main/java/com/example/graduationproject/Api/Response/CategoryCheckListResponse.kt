package com.example.graduationproject.Api.Response

import com.google.gson.annotations.SerializedName

data class CategoryCheckListResponse (
    @SerializedName("categoryId")
    val categoryId:Long,
    @SerializedName("categoryName")
    val categoryName:String,
    @SerializedName("productImgUrl")
    val productImgUrl:String,
    @SerializedName("productName")
    val productName:String,
    @SerializedName("productPrice")
    val productPrice:Long

        )