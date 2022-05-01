package com.example.graduationproject.Api.Request

data class MartAddRequest (
    val martName:String,
    val longitude:String,
    val latitude:String,
    val startDt:String,
    val martAddress:String,
    val openTime:String,
    val closeTime:String,
    val requestYn:String
)