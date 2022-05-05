package com.example.graduationproject.Api.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MartInfoResponse(
    @SerializedName("martName")
    val martName:String,
    @SerializedName("martNumber")
    val martNumber:String,
    @SerializedName("requestYn")
    val requestYn:Long,
    @SerializedName("martAddress")
    val martAddress: String
)
