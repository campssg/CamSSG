package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.UserSigning.UserLoginRequest
import com.example.graduationproject.UserSigning.UserLoginResponse
import com.example.graduationproject.databinding.Activity1passchangeBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class UserPassChangeActivity : AppCompatActivity() {
    private lateinit var binding: Activity1passchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1passchangeBinding.inflate(layoutInflater)
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

        val service = retrofit.create(UserPassChange::class.java)

        val changeBtn = binding.pass1submit

        changeBtn.setOnClickListener {
            val recentPassword = binding.nowpass1input.text.toString()
            val newPassword = binding.newpass1input.text.toString()

            val data = UserPassRequest(newPassword, recentPassword)


            service.change_pass(data).enqueue(object : Callback<UserPassResponse> {
                override fun onResponse(call: Call<UserPassResponse>, response: Response<UserPassResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("변경 완료","${result}")
                        Log.d("변경","${newPassword}")

                        // 변경완료시 마이페이지 화면으로 돌아감
                        finish()
                    } else {
                        Log.d("변경","실패")
                        Log.d("변경","${newPassword}")
                        Toast.makeText(this@UserPassChangeActivity, "변경 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserPassResponse>, t: Throwable) {
                    Log.e("연결실패",t.message.toString())
                }

            })
        }

    }

    interface UserPassChange {
        @PATCH("api/v1/user/update/password")
        @Headers("content-type: application/json", "accept: application/json")
        fun change_pass(
                @Body request: UserPassRequest
        ): Call<UserPassResponse>
    }
}