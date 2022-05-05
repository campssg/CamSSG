package com.example.graduationproject.Api.Response

import com.google.gson.annotations.SerializedName


//마트 카테고리 조회
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




//상품 일괄 등록
data class CheckListResponse(
    @SerializedName("categoryId")
    val categoryId:Long,
    @SerializedName("productImgUrl")
    val productImgUrl:String,
    @SerializedName("productName")
    val productName:String,
    @SerializedName("productPrice")
    val productPrice:Long,
    @SerializedName("productStock")
    val productStock:Long

    )