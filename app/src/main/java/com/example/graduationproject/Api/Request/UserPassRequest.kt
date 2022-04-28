package com.example.graduationproject.Api.Request

data class UserPassRequest(
        val newPassword:String,
        val recentPassword:String
)