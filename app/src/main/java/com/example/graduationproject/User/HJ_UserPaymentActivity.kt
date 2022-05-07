//package com.example.graduationproject.User
//
//import android.app.Application
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.graduationproject.databinding.ActivityHjUserPaymentBinding
//import com.iamport.sdk.data.sdk.IamPortRequest
//import com.iamport.sdk.data.sdk.PayMethod
//import com.iamport.sdk.domain.core.Iamport
//
//
////hj 결제 페이지
//class HJ_UserPaymentActivity : AppCompatActivity() {
//    private lateinit var binding : ActivityHjUserPaymentBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHjUserPaymentBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        Iamport.init(this)
//
//
//    }
//
//
//
//
//}
//
//private fun Iamport.create(app: HJ_UserPaymentActivity) {
//    TODO("Not yet implemented")
//}
//
//
//class BaseApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        Iamport.create(this)
//
//        fun onCreate() {
//            Iamport.init(this)
//
//        }
//
//
//// SDK 종료
//// 명시적으로 화면을 나가는 시점, 꺼지는 시점 등에 추가
//        Iamport.close()
//
//
//// SDK 에 결제 요청할 데이터 구성
//        val request = IamPortRequest(
//            pg = "chai",                                 // PG 사
//            pay_method = PayMethod.trans.name,          // 결제수단
//            name = "여기주문이요",                         // 주문명
//            merchant_uid = "mid_123456",               // 주문번호
//            amount = "3000",                           // 결제금액
//            buyer_name = "홍길동"
//        )
//
//
//// 결제요청
//        Iamport.payment("imp123456", request,
//            approveCallback = { /* (Optional) CHAI 최종 결제전 콜백 함수. */ },
//            paymentResultCallback = { /* 최종 결제결과 콜백 함수. */ })
//
//
//    }
//
//
//}