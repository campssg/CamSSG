package com.example.graduationproject.User

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.MartListAdapter
import com.example.graduationproject.Api.Request.CampWishRequest
import com.example.graduationproject.Api.Response.MartListInfo
import com.example.graduationproject.Api.Response.MartListResponse
import com.example.graduationproject.Api.Response.ResultResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1martsearchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

// 마트 검색 액티비티
class MartSearchActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: Activity1martsearchBinding

    // 지도 객체 및 위치 권한 객체 선언
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<MartListResponse>()
    val martListAdapter = MartListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity1martsearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 네이버 지도 프래그먼트 불러오기
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(NaverMapOptions().locationButtonEnabled(true)).also {
                fm.beginTransaction().add(com.example.graduationproject.R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val TAG:String = "MartSearchActivity"
        Log.e(TAG,"Log---Start:       ")

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvListMart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListMart.adapter = martListAdapter

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 리사이클러뷰 클릭 이벤트 - 데이터 가져오기
        martListAdapter.setItemClickListener(object: MartListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                AlertDialog.Builder(this@MartSearchActivity)
                    .setTitle("상품 검색")
                    .setMessage("${listItems[position].martName}의 상품을 검색하시겠습니까?")
                    .setPositiveButton("예", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val intent = Intent(this@MartSearchActivity, SearchMartItemActivity::class.java)
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

        val service = retrofit.create(MartSearchService::class.java)

        martListAdapter.setItemLongClickListener(object : MartListAdapter.OnItemLongClickListener {
            override fun onLongClick(v: View, position: Int) {
                AlertDialog.Builder(this@MartSearchActivity)
                    .setTitle("즐겨찾기 추가")
                    .setMessage("${listItems[position].martName}을 즐겨찾기에 추가하시겠습니까?")
                    .setPositiveButton("예", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            service.add_wish(listItems[position].martId)
                                .enqueue(object : Callback<ResultResponse> {
                                    override fun onResponse(
                                        call: Call<ResultResponse>,
                                        response: retrofit2.Response<ResultResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val result = response.body()
                                            Log.e("성공", "${result}")
                                            Toast.makeText(this@MartSearchActivity, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Log.d("즐겨찾기 추가", "실패")
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ResultResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e("연결실패", t.message.toString())
                                    }
                                })
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

        // 인탠트 해왔을 경우
        val latitude = intent.getStringExtra("lat")
        val longitude = intent.getStringExtra("long")
        val long = longitude?.toDouble()
        val lat = latitude?.toDouble()
        if (lat != null && long != null) {
            service.search_place(lat, long)
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
                            Log.d("마트 조회", "실패")
                        }
                    }
                    override fun onFailure(
                        call: Call<List<MartListResponse>>,
                        t: Throwable
                    ) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }

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
                ActivityCompat.requestPermissions(this, permissions,
                    MartSearchActivity.LOCATION_PERMISSION_REQUEST_CODE
                )
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
                    val cameraUpdate = CameraUpdate.scrollTo(position)
                    naverMap.moveCamera(cameraUpdate)
                }
                val position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                service.search_place(position.latitude, position.longitude)
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
                                Log.d("마트 조회", "실패")
                            }
                        }
                        override fun onFailure(
                            call: Call<List<MartListResponse>>,
                            t: Throwable
                        ) {
                            Log.e("연결실패", t.message.toString())
                        }
                    })
            }

        }

        binding.btnSearch.setOnClickListener {
            val keyword = binding.etSearchField.text.toString()
            service.search_keyword(keyword)
                .enqueue(object : Callback<MartListInfo> {
                    override fun onResponse(
                        call: Call<MartListInfo>,
                        response: Response<MartListInfo>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")
                            // 리사이클러뷰에 결과 출력 요청 함수
                            AddItemToList(result?.data)
                        } else {
                            Log.d("마트 조회", "실패")
                        }
                    }

                    override fun onFailure(call: Call<MartListInfo>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }
    }

    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<MartListResponse>?) {
        listItems.clear()
        if (searchResult != null) {
            for (martList in searchResult) {
                listItems.add(martList)

                // 검색해 온 위치들 마커 추가
                Marker().apply {
                    position = LatLng(martList.latitude, martList.longitude)
                    map = naverMap
                }
            }
        }
        martListAdapter.notifyDataSetChanged()
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
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow

        // 인탠트 해왔을 경우 가져온 좌표로 카메라 이동
        val latitude = intent.getStringExtra("lat")
        val longitude = intent.getStringExtra("long")
        val long = longitude?.toDouble()
        val lat = latitude?.toDouble()
        if (lat != null && long != null) {
            val camperUpdate = CameraUpdate.scrollTo(LatLng(lat, long))
            naverMap.moveCamera(camperUpdate)
        }
    }
}

// 마트 위치 기반 조회 API 호출
interface MartSearchService {
    @GET("search/mart/{latitude}/{longitude}")
    fun search_place(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude:Double
    ): Call<List<MartListResponse>>

    @GET("mart/{martName}")
    fun search_keyword(
        @Path("martName") martName: String
    ): Call<MartListInfo>

    @POST("wish/add/{martId}")
    fun add_wish(
        @Path("martId") martId: Long
    ): Call<ResultResponse>
}