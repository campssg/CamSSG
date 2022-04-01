package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1campsearchBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

class CampSearchActivity : AppCompatActivity() {
    private lateinit var binding: Activity1campsearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1campsearchBinding.inflate(layoutInflater)
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

        val service = retrofit.create(CampSearchService::class.java)

        // 검색 버튼 클릭 시
        binding.btnSearch.setOnClickListener {
            // 검색 키워드 가져오기
            val keyword = binding.etSearchField.text.toString()
            val page = "1"

            // API 호출
            service.search_keyword(keyword, page)
                .enqueue(object : Callback<CampResult> {
                    override fun onResponse(call: Call<CampResult>, response: retrofit2.Response<CampResult>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")
                            Toast.makeText(this@CampSearchActivity, "총 결과 "+result?.totalCount+"개 조회 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("캠핑장 조회", "실패")
                        }
                    }

                    override fun onFailure(call: Call<CampResult>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }
    }

    // 캠핑장 조회(키워드) API 호출
    interface CampSearchService {
        @GET("camping/{keyword}/{page}")
        fun search_keyword(
            @Path("keyword") keyword:String,
            @Path("page") page:String
        ): Call<CampResult>
    }
}

// API 호출 intercept 해서 JWT 헤더에 담는 interceptor
class AddHeaderJWT constructor(val jwt:String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "Bearer $jwt"
        val newRequest = chain.request().newBuilder().apply {
            addHeader("Authorization", token)
        }.build()
        Log.d("토큰 설정", token)
        return chain.proceed(newRequest)
    }
}