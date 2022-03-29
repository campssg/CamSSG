//package com.example.graduationproject.Api.api
//
//import com.example.graduationproject.UserSigning.RegisterResponse
//import retrofit2.Call
//import retrofit2.http.Field
//import retrofit2.http.FormUrlEncoded
//import retrofit2.http.POST
//////api 호출 안정되면 옮길 예정
//interface Api {
//    @FormUrlEncoded
//    @POST("api/v1/register/guest")
//    fun register(
//        @Field("phoneNumber") phoneNumberTotal: String,
//        @Field("userEmail") email: String,
//        @Field("userName") name: String,
//        @Field("userPassword") Password: String
//    ): Call<RegisterResponse>
//
//    @FormUrlEncoded
//    @POST("/api/v1/register/manager")
//    fun register_mart(
//        @Field("phoneNumber") phoneNumberTotal: String,
//        @Field("userEmail") email: String,
//        @Field("userName") name: String,
//        @Field("userPassword") Password: String
//
//    ): Call<RegisterResponse>
//
//}