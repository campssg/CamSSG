package com.example.graduationproject.UserSigning


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponse(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userNickname")
    val userNickname: String,
    @SerializedName("userPassword")
    val userPassword: String,
    @SerializedName("userRole")
    val userRole: String=""
)