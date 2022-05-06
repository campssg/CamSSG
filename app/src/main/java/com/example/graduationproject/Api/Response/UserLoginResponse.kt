package com.example.graduationproject.UserSigning

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserLoginResponse(
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data?
)

data class Data(
    @SerializedName("token")
    val token:String,
    val userEmail: String,
    val userImgUrl: String,
    val userNickname: String,
    val userName: String,
    val userRole: String
)