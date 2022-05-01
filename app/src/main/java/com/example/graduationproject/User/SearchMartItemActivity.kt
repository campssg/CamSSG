package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.SearchMartItemListAdapter
import com.example.graduationproject.Api.Request.AddCartRequest
import com.example.graduationproject.Api.Response.AddCartResponse
import com.example.graduationproject.Api.Response.ProductList
import com.example.graduationproject.Api.Response.SearchMartCategoryResponse
import com.example.graduationproject.Api.Response.SearchMartItemResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1searchMartItemListBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

// 검색한 마트 클릭 시 -> 해당 마트 상품 조회 페이지
class SearchMartItemActivity : AppCompatActivity() {
    private lateinit var binding: Activity1searchMartItemListBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<ProductList>()
    val searchMartItemListAdapter = SearchMartItemListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity1searchMartItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val TAG:String = "SearchMartItemActivity"
        Log.e(TAG,"Log---Start:       ")

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvListSearchMartItem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListSearchMartItem.adapter = searchMartItemListAdapter

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 장바구니 이미지 클릭 이벤트
        binding.cartImg.setOnClickListener {
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

        val service = retrofit.create(SearchMartItemService::class.java)
        val service2 = retrofit.create(AddCartItemService::class.java)

        // 스피너 설정
        val spinner = binding.itemCategory
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item)

        // 마트 클릭해서 인텐트로 넘어오면
        val martId = intent.getLongExtra("martId", 0)
        if (martId != null) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when (position) {
                        1 -> {
                            searchCategory(1, martId)
                        }
                        2 -> {
                            searchCategory(2, martId)
                        }
                        3 -> {
                            searchCategory(3, martId)
                        }
                        4 -> {
                            searchCategory(4, martId)
                        }
                        5 -> {
                            searchCategory(5, martId)
                        }
                        6 -> {
                            searchCategory(6, martId)
                        }
                        7 -> {
                            searchCategory(7, martId)
                        }
                        8 -> {
                            searchCategory(8, martId)
                        }
                        9 -> {
                            searchCategory(9, martId)
                        }
                    }
                }
            }

            service.search_item(martId)
                .enqueue(object : Callback<SearchMartItemResponse> {
                    override fun onResponse(
                        call: Call<SearchMartItemResponse>,
                        response: Response<SearchMartItemResponse>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("조회 완료", "${result}")

                            // 리사이클러뷰에 결과 출력 요청 함수
                            AddItemToList(result!!.data)
                        } else {
                            Log.d("마트 상품 조회", "실패")
                        }
                    }
                    override fun onFailure(call: Call<SearchMartItemResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })

            // 리사이클러뷰 클릭 이벤트
            searchMartItemListAdapter.setItemClickListener(object: SearchMartItemListAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    // 수량 입력 받을 EditText 생성
                    val editText = EditText(this@SearchMartItemActivity)
                    editText.gravity = Gravity.CENTER
                    editText.inputType = InputType.TYPE_CLASS_NUMBER
                    editText.hint = "수량 입력"

                    // 다이얼로그 생성
                    AlertDialog.Builder(this@SearchMartItemActivity)
                        .setTitle("장바구니 담기")
                        .setMessage("담으실 수량을 입력해주세요")
                        .setView(editText)
                        .setPositiveButton("추가", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val count = editText.text.toString()
                                val data = AddCartRequest(count.toInt())
                                service2.add_item(martId, listItems[position].productId, data)
                                    .enqueue(object : Callback<AddCartResponse> {
                                        override fun onResponse(
                                            call: Call<AddCartResponse>,
                                            response: Response<AddCartResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                val result = response.body()
                                                Log.e("성공", "${result}")
                                            } else {
                                                Log.d("장바구니 추가", "실패")
                                            }
                                        }
                                        override fun onFailure(
                                            call: Call<AddCartResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("연결실패", t.message.toString())
                                        }
                                    })
                            }
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                            }
                        })
                        .create()
                        .show()
                }
            })
        }

    }

    private fun searchCategory(categoryId: Long, martId: Long) {

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

        val service = retrofit.create(SearchMartItemService::class.java)

        service.search_category(categoryId, martId)
            .enqueue(object : Callback<SearchMartCategoryResponse> {
                override fun onResponse(
                    call: Call<SearchMartCategoryResponse>,
                    response: Response<SearchMartCategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        // 리사이클러뷰에 결과 출력 요청 함수
                        AddItemToList(result)
                    } else {
                        Log.d("카테고리 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<SearchMartCategoryResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 검색 결과 받아와서 리사이클러뷰에 추가
   private fun AddItemToList(searchResult: SearchMartCategoryResponse?) {
       listItems.clear() // 리스트 초기화
       //결과 리스트 읽어오기
       for (productList in searchResult!!.productList) {
           listItems.add(productList)
       }
       searchMartItemListAdapter.notifyDataSetChanged()
   }
}

interface SearchMartItemService {
    @GET("search/mart/{martId}")
    fun search_item(
        @Path("martId") martId: Long
    ): Call<SearchMartItemResponse>

    @GET("search/mart/category/{categoryId}/{martId}")
    fun search_category(
        @Path("categoryId") categoryId: Long,
        @Path("martId") martId: Long
    ): Call<SearchMartCategoryResponse>
}

interface AddCartItemService {
    @POST("search/mart/{martId}/{productId}")
    fun add_item(
        @Path("martId") martId: Long,
        @Path("productId") productId: Long,
        @Body request: AddCartRequest
    ): Call<AddCartResponse>
}