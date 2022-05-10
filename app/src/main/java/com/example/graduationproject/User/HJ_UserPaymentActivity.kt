package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Response.ResultResponse
import com.example.graduationproject.databinding.ActivityHjUserPaymentBinding
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

class HJ_UserPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHjUserPaymentBinding

    val application_id = "6277defe270180001ef6a3fb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHjUserPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BootpayAnalytics.init(this, application_id)

        val orderId = intent.getStringExtra("orderId")
        val phoneNumber = intent.getStringExtra("phoneNumber").toString()
        val name = intent.getStringExtra("name")
        val pickup_day = intent.getStringExtra("pickup_day")
        val pickup_time = intent.getStringExtra("pickup_time")
        val martName = intent.getStringExtra("martName")
        val total_price = intent.getStringExtra("total_price")

        binding.orderPhoneNumber.text = phoneNumber
        binding.orderName.text = name
        binding.pickupDay.text = pickup_day
        binding.pickupTime.text = pickup_time
        binding.martName.text = martName
        binding.totalPrice.text = total_price

        binding.buttonFirst.setOnClickListener {
            if (orderId != null) {
                goBootpayRequest(orderId, phoneNumber)
            }
        }
    }

    fun goBootpayRequest(orderId:String, phoneNumber:String) {
        val bootUser = BootUser().setPhone(phoneNumber)
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

        val stuck = 1 //재고 있음

        Bootpay.init(this)
            .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
            .setContext(this)
            .setBootUser(bootUser)
            .setBootExtra(bootExtra)
            .setUX(UX.PG_DIALOG)
            .setPG(PG.KCP)
            .setMethod(Method.KAKAO)
            .setName("캠쓱 픽업 상품") // 결제할 상품명
            .setOrderId(orderId) // 결제 고유번호
            .setPrice(100) // 결제할 금액
            .onConfirm { message ->
                if (0 < stuck) Bootpay.confirm(message); // 재고 있음
                else Bootpay.removePaymentWindow(); // 재고 없음 -> 결제창 중간에 캔슬
                Log.d("confirm", message);
            }
            .onDone { message ->
                Log.d("done", message)
            }
            .onReady { message ->
                Log.d("ready", message)
            }
            .onCancel { message ->
                Log.d("cancel", message)
            }
            .onError{ message ->
                Log.d("error", message)
            }
            .onClose { message ->
                Log.d("close", "close")
                change_state(orderId.toLong())
            }
            .request();
    }

    private fun change_state(orderId: Long) {

        // 로그인 후 저장해둔 JWT 토큰 가져오기
        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val jwt = sharedPreferences.getString("jwt", "")

        val client = OkHttpClient.Builder()
            .addInterceptor(AddHeaderJWT(jwt.toString())) // JWT header 달아주는 interceptor 추가
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.13.202:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()

        val service = retrofit.create(StatusService::class.java)

        service.change_status(orderId, "결제완료")
            .enqueue(object : Callback<ResultResponse> {
                override fun onResponse(
                    call: Call<ResultResponse>,
                    response: Response<ResultResponse>
                ) {
                    println(response)
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("변경 완료", "${result}")
                        finish()
                    } else {
                        Log.d("주문 상태 변경", "실패")
                    }
                }

                override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }
}

interface StatusService {
    @PUT("order/{orderId}/{status}")
    fun change_status(
        @Path("orderId") orderId: Long,
        @Path("status") status: String
    ): Call<ResultResponse>
}
