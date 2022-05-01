package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CartComparisonItem
import com.example.graduationproject.CompareCartResponse
import com.example.graduationproject.Adapter.CompareCartAdapter
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1categoryItemListBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

// sh 마트별 장바구니 가격 비교
class UserCategoryItemList : AppCompatActivity() {
    private lateinit var binding : Activity1categoryItemListBinding

    // 리사이클러뷰 어댑터 설정
    val comparelistItems = arrayListOf<CartComparisonItem>()
    val CompareCartAdapter = CompareCartAdapter(comparelistItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1categoryItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        val TAG:String = "CompareCartActivity"
        Log.e(TAG,"Log---Start:       ")

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvListCompareCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListCompareCart.adapter = CompareCartAdapter


        // 장바구니 이미지 클릭 시
        binding.cartImg.setOnClickListener {
            startActivity(Intent(this, UserCartActivity::class.java))
        }

        //장바구니 글씨 클릭 시
        binding.cartTxt.setOnClickListener {
            startActivity(Intent(this, UserCartActivity::class.java))
        }

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

        val service = retrofit.create(CompareCartService::class.java)





    }

    interface CompareCartService {
        @GET("cart/{latitude}/{longitude}")
        fun compare_result(
            @Path("latitude") latitude: String,
            @Path("longitude") longitude: String
        ): Call<CompareCartResponse>
    }
}