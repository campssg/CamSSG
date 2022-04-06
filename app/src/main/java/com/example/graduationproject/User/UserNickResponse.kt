package com.example.graduationproject.User

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserNickResponse(
    @SerializedName("userNickname")
    val userNickname: String
)
