package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserPassResponse (
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