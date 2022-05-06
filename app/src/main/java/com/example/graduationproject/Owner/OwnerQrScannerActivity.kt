package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Response.GetMartInfoResponse
import com.example.graduationproject.CompareCartResponse
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.Activity1QrScannerBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class OwnerQrScannerActivity : AppCompatActivity() {
    private lateinit var binding : Activity1QrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1QrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        initQRcodeScanner()
    }

    // qr코드 주소 반환 시에
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("onActivityResult 실행", "")
        val result : IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if(result.contents == null) {
                // qr코드에 주소가 없거나, 뒤로가기 클릭 시
                Toast.makeText(this@OwnerQrScannerActivity, "QR코드 인증이 취소되었습니다." , Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // qr코드에 주소가 있을때 -> 주소에 관한 Toast 띄우는 함수 호출
                Log.e("qr 스캔 완료 orderId = ", result.contents)
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

                val service = retrofit.create(UpdateOrder::class.java)

                service.UpdateOrder(result.contents)
                    .enqueue(object : Callback<GetMartInfoResponse> {
                        override fun onResponse(
                            call: Call<GetMartInfoResponse>,
                            response: retrofit2.Response<GetMartInfoResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@OwnerQrScannerActivity, "픽업 처리를 완료하였습니다." , Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<GetMartInfoResponse>, t: Throwable) {
                            Log.e("연결실패", t.message.toString())
                        }
                    })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initQRcodeScanner() {
        Log.e("initQRcodeScanner 실행", "")
        val integrator  = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.setPrompt("QR코드를 인증해주세요.")
        integrator.initiateScan()
    }

    interface UpdateOrder {
        @PUT("order/{orderId}/픽업완료")
        fun UpdateOrder(
            @Path("orderId") orderId: String
        ): Call<GetMartInfoResponse>
    }

    // 뒤로가기 이벤트
    override fun onBackPressed() {
        Toast.makeText(this@OwnerQrScannerActivity, "QR코드 인증이 취소되었습니다." , Toast.LENGTH_SHORT).show()
        finish()
    }
}

