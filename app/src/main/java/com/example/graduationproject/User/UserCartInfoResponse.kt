package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserCartInfoResponse (
    @SerializedName("totalCount")
    val totalCount:Int,
    @SerializedName("totalPrice")
    val totalPrice:Int,
    @SerializedName("cartItemList")
    val cartItemList: List<Cart>
)

data  class Cart (
    @SerializedName("cartItemId")
    val cartItemId:Int,
    @SerializedName("cartItemName")
    val cartItemName:String,
    @SerializedName("cartItemPrice")
    val cartItemPrice:Int,
    @SerializedName("cartItemCount")
    val cartItemCount:Int
)