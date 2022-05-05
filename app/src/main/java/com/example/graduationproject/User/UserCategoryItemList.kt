package com.example.graduationproject.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.CartListAdapter
import com.example.graduationproject.CartComparisonItem
import com.example.graduationproject.CompareCartResponse
import com.example.graduationproject.Adapter.CompareCartAdapter
import com.example.graduationproject.MapActivity.Companion.naverMap
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1categoryItemListBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

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

        // 나중에 캠핑장 즐겨찾기에서 캠핑장 기준 가격비교 추가
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        if (latitude != 0.0 && longitude != 0.0) {
            service.compare_result(latitude, longitude)
                .enqueue(object : Callback<CompareCartResponse> {
                    override fun onResponse(
                        call: Call<CompareCartResponse>,
                        response: retrofit2.Response<CompareCartResponse>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")
                            AddItemToList(result)
                        } else {
                            Log.d("가격 비교 조회", "실패")
                        }
                    }

                    override fun onFailure(call: Call<CompareCartResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        } else {
            // 이하 현재 위치 탐색
            val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            var currentLocation: Location?


            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else { // 권한이 없으면 권한 요구
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // 위치 설정이 켜져있지 않을 경우 위치 설정 화면으로 이동
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                currentLocation = location

                val position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                service.compare_result(position.latitude, position.longitude)
                    .enqueue(object : Callback<CompareCartResponse> {
                        override fun onResponse(
                            call: Call<CompareCartResponse>,
                            response: retrofit2.Response<CompareCartResponse>
                        ) {
                            if (response.isSuccessful) {
                                val result = response.body()
                                Log.e("조회 완료", "${result}")
                                AddItemToList(result)
                            } else {
                                Log.d("가격 비교 조회", "실패")
                            }
                        }

                        override fun onFailure(call: Call<CompareCartResponse>, t: Throwable) {
                            Log.e("연결실패", t.message.toString())
                        }
                    })

            }
        }

        // 리사이클러뷰 클릭시 상세보기 이동
        CompareCartAdapter.setItemClickListener(object : CompareCartAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val intent = Intent(this@UserCategoryItemList, UserCompareCartDetail::class.java)
                intent.putExtra("martName", comparelistItems[position].martName)
                intent.putExtra("martAddress", comparelistItems[position].martAddress)
                intent.putExtra("closeTime", comparelistItems[position].closeTime)
                intent.putExtra("martId", comparelistItems[position].martId)
                startActivity(intent)
            }
        })
    }


    private fun AddItemToList(compareResult: CompareCartResponse?) {
        comparelistItems.clear() // 리스트 초기화
        // 결과 리스트 읽어오기
        for (campingList in compareResult!!.data.cartComparisonList) {
            comparelistItems.add(campingList)
        }
        CompareCartAdapter.notifyDataSetChanged()
    }

    interface CompareCartService {
        @GET("cart/{latitude}/{longitude}")
        fun compare_result(
            @Path("latitude") latitude: Double,
            @Path("longitude") longitude: Double
        ): Call<CompareCartResponse>
    }
}