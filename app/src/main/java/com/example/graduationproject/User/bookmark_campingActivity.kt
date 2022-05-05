package com.example.graduationproject.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.CampWishListAdapter
import com.example.graduationproject.Api.Response.CampWishResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1bookmarkCampingBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class bookmark_campingActivity : AppCompatActivity() {
    private lateinit var binding: Activity1bookmarkCampingBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<CampWishResponse>()
    val campWishListAdapter = CampWishListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1bookmarkCampingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러 뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = campWishListAdapter

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

        val service = retrofit.create(WishCampService::class.java)

        campWishListAdapter.setItemClickListener(object : CampWishListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(this@bookmark_campingActivity, UserCategoryItemList::class.java)
                intent.putExtra("latitude", listItems[position].latitude)
                intent.putExtra("longitude", listItems[position].longitude)
                startActivity(intent)
            }
        })

        service.get_wishlist()
            .enqueue(object : Callback<List<CampWishResponse>> {
                override fun onResponse(
                    call: Call<List<CampWishResponse>>,
                    response: Response<List<CampWishResponse>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        // 리사이클러뷰에 결과 출력 요청 함수
                        AddItemToList(result!!)
                    }
                }

                override fun onFailure(call: Call<List<CampWishResponse>>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<CampWishResponse>) {
        listItems.clear() // 리스트 초기화
        // 결과 리스트 읽어오기
        for (campingList in searchResult) {
            listItems.add(campingList)
        }
        campWishListAdapter.notifyDataSetChanged()
    }
}

interface WishCampService {
    @GET("wish/get/camp")
    fun get_wishlist(): Call<List<CampWishResponse>>
}