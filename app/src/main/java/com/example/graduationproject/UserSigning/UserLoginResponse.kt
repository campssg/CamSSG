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
    val data: Data
)