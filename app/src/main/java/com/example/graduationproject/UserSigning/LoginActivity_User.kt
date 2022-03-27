package com.example.graduationproject.UserSigning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.RegisterActivity2
import com.example.graduationproject.User.UserMainActivity
import com.example.graduationproject.databinding.Activity1loginBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.connection.ConnectInterceptor.intercept
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

//헤더에 넣기?
/*fun getHeaders(): Map<String, String>? {
    val params: MutableMap<String, String> = HashMap()
    params.put("Authorization", "Bearer" + yourToken)
    return params
}*/


//로그인 하기 위해 서버에 아이디, 비밀번호 전달
interface LoginService{
    @FormUrlEncoded
    @POST("/api/v1/login")
    fun login(
            @Field("userEmail") id: String,
            @Field("userPassword") password: String
    ): Call<UserLoginResponse>
}



//로그인 시 프론트 아이디, 비밀번호 전달 -> 맞으면 백엔드에서 jwt 발행
// -> 백엔드에서 헤더에 해당 코드 담아서 반환 -> 프론트 변환 후 저장
// -> 후에 계속 헤더에 jwt 포함하여 전달



//사용자 로그인
class LoginActivity_User : AppCompatActivity() {
    private lateinit var binding: Activity1loginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1loginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        //변수
        val id = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()

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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        //로그인 버튼 클릭
        loginBtn.setOnClickListener {
            service.login(id, password)
                .enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                        val result = response.body()
                        Log.e("로그인 완료","${result}")
                        val intent = Intent(this@LoginActivity_User, UserMainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        Log.e("다시 로그인 필요",t.message.toString())
                    }

                })

        }
    }
}