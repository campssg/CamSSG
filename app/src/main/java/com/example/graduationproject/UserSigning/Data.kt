package com.example.graduationproject.UserSigning

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("token")
    val token: String
)