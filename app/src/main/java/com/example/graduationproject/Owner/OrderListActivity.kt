package com.example.graduationproject.Owner

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.MartOrderListAdapter
import com.example.graduationproject.Api.Response.MartOrderListResponse
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityOrderlistBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

// 마트 운영자 주문 현황 조회
class OrderListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOrderlistBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<MartOrderListResponse>()
    val martOrderListAdapter = MartOrderListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.noticeRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.noticeRV.adapter = martOrderListAdapter

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

        val service = retrofit.create(MartOrderListService::class.java)

        val martId = intent.getLongExtra("martId", 0)

        service.total_order(martId)
            .enqueue(object : Callback<List<MartOrderListResponse>> {
                override fun onResponse(
                    call: Call<List<MartOrderListResponse>>,
                    response: Response<List<MartOrderListResponse>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        AddItemToList(result)
                    } else {
                        Log.d("마트 주문현황 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<List<MartOrderListResponse>>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 조회 결과 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<MartOrderListResponse>?) {
        listItems.clear()
        for (orderList in searchResult!!) {
            listItems.add(orderList)
        }
        martOrderListAdapter.notifyDataSetChanged()
    }
}

interface MartOrderListService {
    @GET("order/mart/{martId}")
    fun total_order(
        @Path("martId") martId: Long
    ): Call<List<MartOrderListResponse>>
}