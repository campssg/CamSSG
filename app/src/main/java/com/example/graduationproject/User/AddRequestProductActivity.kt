package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Request.AddRequestProductRequest
import com.example.graduationproject.Api.Response.AddCartResponse
import com.example.graduationproject.Api.Response.AddRequestProductResponse
import com.example.graduationproject.Owner.ManagerItemListActivity
import com.example.graduationproject.databinding.Activity1addRequestBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// sh 요청할 상품 선택 -> 요청 상품 등록
class AddRequestProductActivity : AppCompatActivity() {
    private lateinit var binding : Activity1addRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1addRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

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

        val service = retrofit.create(AddRequestProductService::class.java)

        // 결제 전에 주문번호 받아서 인텐트 넘어옴
        val orderId = intent.getLongExtra("orderId", 0)


        binding.RequestBtn.setOnClickListener {
            // 빈칸이 있을 경우
            if (binding.nameRequestItem.text.toString()=="" || binding.priceRequestItem.text.toString()==""
                    || binding.countRequestItem.text.toString()=="" || binding.referenceRequestItem.text.toString()==""){
                Toast.makeText(this@AddRequestProductActivity, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            }

            // 빈칸 X => 요청 물품 추가 => 추가완료 시 더 추가할 것인지 물어봄
            else{
                // 갯수
                val requestedProductCount = binding.countRequestItem.text.toString()
                val count = requestedProductCount.toInt()

                // 이름
                val requestedProductName = binding.nameRequestItem.text.toString()

                // 가격
                val requestedProductPrice = binding.priceRequestItem.text.toString()
                val price = requestedProductPrice.toInt()

                // 참고링크
                val requestedProductReference = binding.referenceRequestItem.text.toString()

                val data = AddRequestProductRequest(orderId, count, requestedProductName, price, requestedProductReference)
                service.add_request(data)
                        .enqueue(object : Callback<AddRequestProductResponse> {
                            override fun onResponse(
                                    call: Call<AddRequestProductResponse>,
                                    response: Response<AddRequestProductResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val result = response.body()
                                    Log.e("성공", "${result}")

                                    Toast.makeText(this@AddRequestProductActivity, "상품 요청 성공!", Toast.LENGTH_SHORT).show()

                                    // 요청 상품 추가 여부
                                    // 예 -> editText 빈칸 처리
                                    // 아니오 -> 액티비티 종료 or 메인화면
                                    AlertDialog.Builder(this@AddRequestProductActivity)
                                            .setTitle("요청상품 추가 등록")
                                            .setMessage("다른 상품을 또 요청하시겠습니까?")
                                            .setPositiveButton("예", object : DialogInterface.OnClickListener{
                                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                                    binding.nameRequestItem.setText(null)
                                                    binding.priceRequestItem.setText(null)
                                                    binding.countRequestItem.setText(null)
                                                    binding.referenceRequestItem.setText(null)
                                                }
                                            })
                                            .setNegativeButton("아니오", object : DialogInterface.OnClickListener{
                                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                                    // 현재 액티비티만 종료
                                                    // finish()

                                                    // 메인
                                                    // startActivity(Intent(this@AddRequestProductActivity, UserMainActivity::class.java)
                                                }
                                            })
                                } else {
                                    Log.d("요청 상품 등록", "실패")
                                }
                            }
                            override fun onFailure(
                                    call: Call<AddRequestProductResponse>,
                                    t: Throwable
                            ) {
                                Log.e("연결실패", t.message.toString())
                            }
                        })

            }
        }
    }

    interface AddRequestProductService {
        @POST("request/add")
        @Headers("content-type: application/json", "accept: application/json")
        fun add_request(
                @Body request: AddRequestProductRequest
        ): Call<AddRequestProductResponse>
    }


}