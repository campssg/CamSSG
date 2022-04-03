package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1cartlistBinding
import com.example.graduationproject.databinding.Activity1mypageBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class UserCartActivity : AppCompatActivity() {
    private lateinit var binding: Activity1cartlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1cartlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
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

        val service = retrofit.create(UserCart::class.java)

        // 사용자 장바구니 API 호출
        // API 호출
        service.get_userCart()
            .enqueue(object : Callback<UserCartInfoResponse> {
                override fun onResponse(call: Call<UserCartInfoResponse>, response: retrofit2.Response<UserCartInfoResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")
                    } else {
                        Log.d("사용자 카트 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserCartInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    interface UserCart {
        @GET("api/v1/cart/info")
        fun get_userCart(): Call<UserCartInfoResponse>
    }
}