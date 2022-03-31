package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CampResult(
    @SerializedName("pageNo")
    val pageNo:String,
    @SerializedName("totalCount")
    val totalCount:String,
    @SerializedName("campingLists")
    val campingLists:List<CampList>
)

data class CampList(
    @SerializedName("address")
    val address:String,
    @SerializedName("detailAddress")
    val detailAddress:String,
    @SerializedName("campName")
    val campName:String,
    @SerializedName("homepage")
    val homepage:String,
    @SerializedName("mapX")
    val mapX:String,
    @SerializedName("mapY")
    val mapY:String,
    @SerializedName("tel")
    val tel:String,
    @SerializedName("img")
    val img:String
)