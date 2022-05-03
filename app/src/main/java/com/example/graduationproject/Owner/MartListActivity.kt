package com.example.graduationproject.Owner

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.MartListAdapter
import com.example.graduationproject.Api.Response.MartListInfo
import com.example.graduationproject.Api.Response.MartListResponse
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityMartListBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// 상품 등록하기 전 마트 선택 액티비티
class MartListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMartListBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<MartListResponse>()
    val martListAdapter = MartListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMartListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val TAG:String = "MartListActivity"
        Log.e(TAG,"Log---Start:       ")

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvListMart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListMart.adapter = martListAdapter

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

        val service = retrofit.create(MartListService::class.java)

        service.mart_list()
            .enqueue(object : Callback<MartListInfo> {
                override fun onResponse(
                    call: Call<MartListInfo>,
                    response: Response<MartListInfo>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        // 리사이클러뷰에 결과 출력 요청 함수
                        AddItemToList(result!!.data)
                    } else {
                        Log.d("마트 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<MartListInfo>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })

        val menu = intent.getIntExtra("menu", 0)

        // 리사이클러뷰 클릭 이벤트 - 데이터 가져오기
        martListAdapter.setItemClickListener(object : MartListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (menu == 1) {
                    val intent = Intent(this@MartListActivity, SelectAddOneOrManyActivity::class.java)
                    intent.putExtra("martId", listItems[position].martId)
                    startActivity(intent)
                } else if (menu == 2) {
                    val intent = Intent(this@MartListActivity, ManagerItemListActivity::class.java)
                    intent.putExtra("martId", listItems[position].martId)
                    startActivity(intent)
                } else if (menu == 3) {
                    //val intent = Intent(this@MartListActivity, ManageItemActivity::class.java)
                    //intent.putExtra("martId", listItems[position].martId)
                    //startActivity(intent)
                } else if (menu == 4) {
                    val intent = Intent(this@MartListActivity, EditMartActivity::class.java)
                    intent.putExtra("martId", listItems[position].martId)
                    startActivity(intent)
                } else if (menu == 5) {
                    val intent = Intent(this@MartListActivity, OrderListActivity::class.java)
                    intent.putExtra("martId", listItems[position].martId)
                    startActivity(intent)
                }
            }
        })
    }

    // 검색 결과 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<MartListResponse>) {
        listItems.clear()
        if (searchResult != null) {
            for (martList in searchResult) {
                listItems.add(martList)
            }
        }
        martListAdapter.notifyDataSetChanged()
    }
}

// 내 마트 목록 조회 API 호출
interface MartListService {
    @GET("mart/info")
    fun mart_list(): Call<MartListInfo>
}