package com.example.graduationproject.User

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1paymentBinding
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class UserPaymentActivity : AppCompatActivity() {
    private lateinit var binding: Activity1paymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1paymentBinding.inflate(layoutInflater)
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

        val service = retrofit.create(UserCartActivity.UserCart::class.java)

        val sharedPreferences2 = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = sharedPreferences2.getString("userName","")
        val userNum = sharedPreferences2.getString("userNum", "")

        binding.userName.setText(userName)
        binding.userNumber.setText(userNum)

        val sharedPreferences3 = getSharedPreferences("putime", MODE_PRIVATE)
        val putime = sharedPreferences3.getString("text", "")

        binding.puTime.setText(putime)

        binding.changePut.setOnClickListener {
            startActivity(Intent(this, PickupTimeActivity::class.java))
        }



    }
}