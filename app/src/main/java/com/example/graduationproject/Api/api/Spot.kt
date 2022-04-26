package com.example.graduationproject.Api.api

import androidx.annotation.Keep

@Keep
data class Spot (
    val id: Int,
    val latitude: String,
    val longitude: String,
    val type: Int
        )