package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1mypageBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: Activity1mypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1mypageBinding.inflate(layoutInflater)
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

        val service = retrofit.create(UserInfo::class.java)

        // 사용자 정보 조회 API 호출
        service.get_userInfo()
            .enqueue(object : Callback<UserInfoResponse>{
                override fun onResponse(
                    call: Call<UserInfoResponse>,
                    response: Response<UserInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회완료", "${result}")

                        // 조회한 결과로 텍스트 설정
                        binding.mypage1nametxt.setText(result?.userName)
                        binding.mypage1emailtxt.setText(result?.userEmail)
                        if (result?.userNickname!=null) {
                            binding.mypage1nickedit.setText(result.userNickname)
                        }
                    } else {
                        Log.d("사용자 정보 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    interface UserInfo {
        @GET("api/v1/user/info")
        fun get_userInfo(): Call<UserInfoResponse>
    }
}