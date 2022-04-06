package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1mypageBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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

                        val sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
                        sharedPreferences.edit().putString("userName", result?.userName).apply()

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

        val retrofit2 = Retrofit.Builder()
                .baseUrl("http://13.124.13.202:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

        val service2 = retrofit2.create(UserNickChange::class.java)

        val nickchangeBtn = binding.mypage1nickbtn

        // 닉네임 수정
        nickchangeBtn.setOnClickListener {

            val userNick = binding.mypage1nickedit.text.toString()
            val data = UserNickRequest(userNick)

            service2.change_nickname(data).enqueue(object : Callback<UserNickResponse> {
                override fun onResponse(call: Call<UserNickResponse>, response: Response<UserNickResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("수정 완료","${result}")
                        dialog("Ok")
                    } else {
                        Log.d("수정","실패")
                        Log.d("수정","${userNick}")
                        Toast.makeText(this@UserInfoActivity, "수정에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserNickResponse>, t: Throwable) {
                    Log.e("연결실패",t.message.toString())
                }

            })

        }

        // 비밀번호 변경
        binding.mypage1pass.setOnClickListener {
            val intent = Intent(this, UserPassChangeActivity::class.java)
            startActivity(intent)
        }
    }

    interface UserInfo {
        @GET("api/v1/user/info")
        fun get_userInfo(): Call<UserInfoResponse>
    }

    interface UserNickChange{
        @PATCH("api/v1/user/update/nickname")
        @Headers("content-type: application/json", "accept: application/json")
        fun change_nickname(
                @Body request: UserNickRequest
        ): Call<UserNickResponse>
    }

    // 알림창
    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)
        if (type.equals("Ok")){
            dialog.setTitle("닉네임 수정")
            dialog.setMessage("완료되었습니다")
        }

        val dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d("닉네임", "다이얼로그")
                }

            }
        }
        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }
}