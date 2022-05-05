package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.MartListAdapter
import com.example.graduationproject.Api.Response.MartListResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1bookmarkMartBinding
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

//HJ 마이페이지-마트 즐겨찾기
class bookmark_martActivity : AppCompatActivity() {
    private lateinit var binding: Activity1bookmarkMartBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<MartListResponse>()
    val martListAdapter = MartListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1bookmarkMartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = martListAdapter

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 리사이클러뷰 클릭 이벤트 - 데이터 가져오기
        martListAdapter.setItemClickListener(object: MartListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                AlertDialog.Builder(this@bookmark_martActivity)
                    .setTitle("상품 검색")
                    .setMessage("${listItems[position].martName}의 상품을 검색하시겠습니까?")
                    .setPositiveButton("예", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val intent = Intent(this@bookmark_martActivity, SearchMartItemActivity::class.java)
                            intent.putExtra("martId", listItems[position].martId)
                            startActivity(intent)
                        }
                    })
                    .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }
                    })
                    .create()
                    .show()
            }
        })

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

        val service = retrofit.create(WishMartService::class.java)

        service.get_wishlist()
            .enqueue(object : Callback<List<MartListResponse>> {
                override fun onResponse(
                    call: Call<List<MartListResponse>>,
                    response: Response<List<MartListResponse>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        // 리사이클러뷰에 결과 출력 요청 함수
                        AddItemToList(result)
                    } else {
                        Log.d("마트 즐겨찾기 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<List<MartListResponse>>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<MartListResponse>?) {
        listItems.clear()
        if (searchResult != null) {
            for (martList in searchResult) {
                listItems.add(martList)
            }
        }
        martListAdapter.notifyDataSetChanged()
    }
}

interface WishMartService {
    @GET("wish/get/mart")
    fun get_wishlist(): Call<List<MartListResponse>>
}