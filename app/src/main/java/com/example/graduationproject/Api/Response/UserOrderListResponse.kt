package com.example.graduationproject.Api.Response

import java.time.LocalDateTime

data class UserOrderListResponse(
    val martName: String,
    val orderId: Long,
    val orderState: String,
    val pickup_day: String,
    val pickup_time: String,
    val totalPrice: Int
)




//주문 상세 내역 조회
data class UserDetailOrderListResponse(
    val cartItemPrice:Long,
    val charge:Long,
    val martName: String,
    val orderId:Long,
    val totalPrice:Long,
    val orderItemList:List<orderlist>,

    val orderState: String,
    val order_phoneNumber: String,
    val pickup_day : String,
    val pickup_time: String,
    val qrcode_url:String,
    val userName:String,

    val requestedProductList:List<RequestedProductList>
)

data class orderlist(
    val orderItemCount:Long,
    val orderItemId:Long,
    val orderItemName:String,
    val orderItemPrice:Long
)

data class RequestedProductList(
    val requestedProductCount:Int,
    val requestedPRoductId:Long,
    val requestedPRoductName:String,
    val requestedProductPrice:Long
)