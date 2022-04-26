package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CamplistAdapter
import com.example.graduationproject.databinding.Activity1campsearchBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.android.synthetic.main.recyclerview_camplist.view.*
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
import android.util.Log

class CampSearchActivity : AppCompatActivity(), OnMapReadyCallback {
//    private lateinit var viewModel :MainViewModel
    //private val camplistAdapter by lazy { CamplistAdapter() }


    companion object {
        lateinit var naverMap: NaverMap
    }

    private lateinit var mapView: MapView

    // 리사이클러뷰 어댑터 설정
    private val listItems = arrayListOf<CampList>()
    private val camplistAdapter = CampListAdapter(listItems)

    private lateinit var binding: Activity1campsearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mapView = binding.
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)
        binding = Activity1campsearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val TAG:String = "CampSearchActivity"
        Log.e(TAG,"Log---Start:       ")
        // 리사이클러 뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = camplistAdapter

        //어댑터 연결
        //binding.rvList.adapter = camplistAdapter
        //binding.rvList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

//        val repository = Repository()
//        val viewModelFactory = MainViewModelFactory(repository)
//        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)


        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


//        val Camplist = arrayListOf<CampList>(
//            CampList("오리로","","010-8734-7954","","","","010-8734-7954","")
//        )

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


        // 검색 버튼 클릭 시
        binding.btnSearch.setOnClickListener {


            // 검색 키워드 가져오기
            val keyword = binding.etSearchField.text.toString()
            val page = "1"

            val campName = "1"
            val tel = "1"
            val address = "1"

//             fun setData(status:String) {
//                service(status){ b, s, list ->
//                    list?.let{
//                        //리스트가 널이 아니면 실행
//
//                        (binding.rvList.adapter as CamplistAdapter).setDatas(list) //우리가 만든 얻댑터로 캐스팅
//                    }
//                }
//            }
//
//             fun setRV() {
//                binding.rvList.apply {
//                    adapter= CamplistAdapter() //데이터 넣어주어야함
//                        .apply {
//
//                        }
//
//
//                }
//            }
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
//                            Toast.makeText(
//                                this@CampSearchActivity,
//                                "총 결과 " + result?.totalCount + "개 조회 성공",
//                                Toast.LENGTH_SHORT
//                            ).show()

//                            Toast.makeText(
//                                this@CampSearchActivity,
//                                "총 결과 " + result?.totalCount + "개 조회 성공" + "첫번째 결과 :" + result?.campingLists,
//                                Toast.LENGTH_SHORT
//                            ).show()

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


//
            // 받아온 값을 리싸이클러뷰에 보여줌
//                viewModel.getCustomPosts2(Integer.parseInt(binding.editTextView.text.toString()),"id","asc")
//                viewModel.myCustomPosts2.observe(this, Observer {
//                    if(it.isSuccessful){
//                        camplistAdapter.setData(it.body()!!)
//                    }
//                    else{
//                        Toast.makeText(this,it.code(), Toast.LENGTH_SHORT).show()
//                    }
//                })

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

    override fun onMapReady(p0: NaverMap) {
        CampSearchActivity.naverMap = naverMap

        var camPos = CameraPosition(
            LatLng(34.38, 128.55),
            9.0
        )
        naverMap.cameraPosition = camPos
    }


}

private fun <T> Call<T>.enqueue(callback: Callback<CampList>) {

}


// 캠핑장 조회(키워드) API 호출
interface CampSearchService {
    @GET("camping/{keyword}/{page}")
    fun search_keyword(
        @Path("keyword") keyword: String,
        @Path("page") page: String
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


