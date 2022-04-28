package com.example.graduationproject.UserSigning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.Api.Request.UserLoginRequest
import com.example.graduationproject.Owner.OwnerMainActivity
import com.example.graduationproject.databinding.ActivityLoginBinding
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

class LoginActivity_Mart : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val loginBtn = binding.LoginButton
        val register = binding.textView9


        val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://13.124.13.202:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

        val service = retrofit.create(LoginService::class.java)

        //회원가입 클릭
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity_User::class.java)
            startActivity(intent)
        }


        //로그인 버튼 클릭
        loginBtn.setOnClickListener {
            //변수
            val userEmail = binding.editTextTextEmailAddress.text.toString()
            val userPassword = binding.editTextPassword.text.toString()

            // 변수 가져와서 json body 생성 -> json 자체를 요청 변수로 전송
            val data = UserLoginRequest(userEmail, userPassword)
            service.login(data)
                    .enqueue(object : Callback<UserLoginResponse> {
                        override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                            if (response.isSuccessful) {
                                val result = response.body()
                                Log.e("로그인 완료","${result}")

                                // 받아온 jwt 토큰 가져와서 preference에 저장
                                val token = result?.data?.token
                                val sharedPreference = getSharedPreferences("token", MODE_PRIVATE)
                                val editor = sharedPreference.edit()
                                editor.putString("jwt", token.toString())
                                editor.apply()

                                Toast.makeText(this@LoginActivity_Mart, "마트 운영자 로그인 성공", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity_Mart, OwnerMainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.d("로그인","실패")
                                Toast.makeText(this@LoginActivity_Mart, "아이디, 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                            Log.e("연결실패",t.message.toString())
                        }

                    })

        }
    }

    // 엔드포인트 앞에 / 삭제
    // 전송값을 @Field 에서 @Body로 변경 => @Body는 json 자체를 전송하므로 반드시 Header에 "content-type" 명시
    // 로그인 하기 위해 서버에 아이디, 비밀번호 전달
    interface LoginService{
        @POST("user/login")
        @Headers("content-type: application/json", "accept: application/json")
        fun login(
                @Body request: UserLoginRequest
        ): Call<UserLoginResponse>
    }

}