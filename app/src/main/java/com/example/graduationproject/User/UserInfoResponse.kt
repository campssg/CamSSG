package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserInfoResponse(
    @SerializedName("userEmail")
    val userEmail:String,
    @SerializedName("userName")
    val userName:String,
    @SerializedName("userNickname")
    val userNickname: String
)
