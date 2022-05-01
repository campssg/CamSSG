package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserCartInfoResponse (
    @SerializedName("statusCode")
    val statusCode:String,
    @SerializedName("message")
    val message:String,
    @SerializedName("data")
    val data:Cart,
)
data class Cart (
    @SerializedName("totalCount")
    val totalCount:Int,
    @SerializedName("totalPrice")
    val totalPrice:Int,
    @SerializedName("cartItemList")
    val cartItemList: List<CartItem>
)

data class CartItem (
    @SerializedName("cartItemId")
    val cartItemId:Int,
    @SerializedName("cartItemName")
    val cartItemName:String,
    @SerializedName("cartItemPrice")
    val cartItemPrice:Int,
    @SerializedName("cartItemCount")
    val cartItemCount:Int,
    val cartItemImgUrl: String
)