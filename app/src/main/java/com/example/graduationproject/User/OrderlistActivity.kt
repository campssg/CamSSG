package com.example.graduationproject.User

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.UserOrderListAdapter
import com.example.graduationproject.Api.Response.UserOrderListResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1orderlistBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

//주문내역

class OrderlistActivity : AppCompatActivity() {
    private lateinit var binding : Activity1orderlistBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<UserOrderListResponse>()
    val userOrderListAdapter = UserOrderListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1orderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.noticeRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.noticeRV.adapter = userOrderListAdapter

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

        val service = retrofit.create(UserOrderListService::class.java)

        val spinner = binding.orderlistSpinner
        ArrayAdapter.createFromResource(
            this, R.array.orderlist_spinner_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    1 -> {

                    }
                }
            }
        }

        // 전체 주문 내역 조회
        service.total_order()
            .enqueue(object : Callback<List<UserOrderListResponse>> {
                override fun onResponse(
                    call: Call<List<UserOrderListResponse>>,
                    response: Response<List<UserOrderListResponse>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("성공", "${result}")
                        // AddItemToList(result)
                    } else {
                        Log.d("주문 내역 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<List<UserOrderListResponse>>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 조회 결과 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<UserOrderListResponse>?) {
        listItems.clear()
        for (orderList in searchResult!!) {
            listItems.add(orderList)
        }
        userOrderListAdapter.notifyDataSetChanged()
    }
}

interface UserOrderListService {
    @GET("order/user")
    fun total_order():Call<List<UserOrderListResponse>>
}


