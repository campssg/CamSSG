package com.example.graduationproject.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.databinding.Activity1campsearchBinding
import com.naver.maps.geometry.LatLng
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.http.*

class CampSearchActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: Activity1campsearchBinding

    // 지도 객체 및 위치 권한 객체 선언
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    // 리사이클러뷰 어댑터 설정
     val listItems = arrayListOf<CampList>()
     val camplistAdapter = CampListAdapter(listItems)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity1campsearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 네이버 지도 프래그먼트 불러오기
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(com.example.graduationproject.R.id.mapmap) as MapFragment?
            ?: MapFragment.newInstance(NaverMapOptions().locationButtonEnabled(true)).also {
                fm.beginTransaction().add(com.example.graduationproject.R.id.mapmap, it).commit()
            }
        mapFragment.getMapAsync(this)

        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val TAG:String = "CampSearchActivity"
        Log.e(TAG,"Log---Start:       ")
        // 리사이클러 뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = camplistAdapter

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        // 리사이클러뷰 클릭 이벤트 - 데이터 가져오기
        camplistAdapter.setItemClickListener(object: CampListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(this@CampSearchActivity,
                    "${listItems[position].mapY}\n" + "${listItems[position].mapX}\n",
                    Toast.LENGTH_SHORT).show()

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

        val service = retrofit.create(CampSearchService::class.java)
        val service2 = retrofit.create(CampListService::class.java)

        // 위치 검색 버튼 클릭 시 현재 위치 좌표 반환
        binding.searchAround.setOnClickListener {
            var currentLocation: Location?
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
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

                // 현재 위치 좌표 출력
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                }
                val position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                Toast.makeText(this@CampSearchActivity, "위도:${position.latitude}, 경도:${position.longitude}", Toast.LENGTH_SHORT).show()
                service.search_place(page="1", position.latitude.toString(), position.longitude.toString())
                    .enqueue(object : Callback<CampResult> {
                        override fun onResponse(
                            call: Call<CampResult>,
                            response: retrofit2.Response<CampResult>
                        ) {
                            if (response.isSuccessful) {
                                val result = response.body()
                                Log.e("조회 완료", "${result}")

                                // 리사이클러뷰에 결과 출력 요청 함수
                                AddItemToList(result)
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


        // 검색 버튼 클릭 시
        binding.btnSearch.setOnClickListener {


            // 검색 키워드 가져오기
            val keyword = binding.etSearchField.text.toString()
            val page = "1"

            val campName = "1"
            val tel = "1"
            val address = "1"


            // API 호출

//                             API 호출(캠핑장 결과)
            service2.search_result(campName, tel, address)
                .enqueue(object : Callback<CampList> {
                    override fun onResponse(
                        call: Call<CampList>,
                        response: retrofit2.Response<CampList>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")

//                            binding.rvList.layoutManager = LinearLayoutManager(this)
//                            binding.rvList.setHasFixedSize(true)


                        } else {
                            Log.d("캠핑장 조회", "실패")

                        }
                    }

                    override fun onFailure(call: Call<CampList>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })

            service.search_keyword(keyword, page)
                .enqueue(object : Callback<CampResult> {
                    override fun onResponse(
                        call: Call<CampResult>,
                        response: retrofit2.Response<CampResult>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")


                            // 리사이클러뷰에 결과 출력 요청 함수
                            AddItemToList(result)
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

    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: CampResult?) {
        listItems.clear() // 리스트 초기화
        // 결과 리스트 읽어오기
        for (campingList in searchResult!!.campingLists) {
            listItems.add(campingList)
        }

        camplistAdapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<CampList>) {

}


// 캠핑장 조회(키워드/위치) API 호출
interface CampSearchService {
    @GET("camping/{keyword}/{page}")
    fun search_keyword(
        @Path("keyword") keyword: String,
        @Path("page") page: String
    ): Call<CampResult>

    @GET("camping/place/{page}")
    fun search_place(
        @Path("page") page: String,
        @Query("mapY") mapY: String,
        @Query("mapX") mapX: String
    ): Call<CampResult>
}

// 캠핑장 조회(키워드) API 호출
interface CampListService {
    @GET("camping/{campName}/{tel}/{address}")
    fun search_result(
        @Path("campName") campName: String,
        @Path("tel") tel: String,
        @Path("address") address: String
    ): Call<CampResult>
}

// API 호출 intercept 해서 JWT 헤더에 담는 interceptor
class AddHeaderJWT constructor(val jwt: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "Bearer $jwt"
        val newRequest = chain.request().newBuilder().apply {
            addHeader("Authorization", token)
        }.build()
        Log.d("토큰 설정", token)
        return chain.proceed(newRequest)
    }
}