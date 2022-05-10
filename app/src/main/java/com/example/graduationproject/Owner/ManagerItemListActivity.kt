package com.example.graduationproject.Owner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.ManagerItemListAdapter
import com.example.graduationproject.Api.Response.*
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityManageItemListBinding
import kotlinx.android.synthetic.main.activity_manage_item_main.view.*
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

//SH 마트 상품 관리 (상품 조회 및 마트 상품 삭제)
class ManagerItemListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManageItemListBinding

    // 리사이클러뷰 어댑터 설정
    // 마트 상품 서치와 같은 리스트 사용
    val listItems = arrayListOf<ProductList2>()
    val ManagerItemListAdapter = ManagerItemListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val TAG:String = "ManagerItemListActivity"
        Log.e(TAG, "Log---Start:       ")

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvListManageItem.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListManageItem.adapter = ManagerItemListAdapter

        // 스피너 설정
        val spinner = binding.itemCategory
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item)


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

        val service = retrofit.create(MartItemService::class.java)  // 마트 상품 조회
        val service2 = retrofit.create(deleteProduct::class.java)   // 마트 상품 삭제
        val service3 = retrofit.create(AddStock::class.java)        // 상품 재고 추가

        // 마트 아이디 가져오기
        val martId = intent.getLongExtra("martId", 0)

        // 카테고리 선택
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

            // 상품 조회
            service.search_item(martId)
                    .enqueue(object : Callback<MartProductResponse> {
                        override fun onResponse(
                                call: Call<MartProductResponse>,
                                response: Response<MartProductResponse>
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

                        override fun onFailure(call: Call<MartProductResponse>, t: Throwable) {
                            Log.e("연결실패", t.message.toString())
                        }
                    })


            // 리사이클러뷰 그냥 클릭
            // 상품 정보 수정 페이지 이동
            // 상품 아이디, 이름, 가격, 재고 인텐트 넘김

            // 위와 같이 진행하려 했는데 기능이 "재고 수정" 이 아니라 "재고 추가" 여서
            // EditText 창으로 추가하는 것으로 변경하였습니다.

            ManagerItemListAdapter.setItemClickListener(object : ManagerItemListAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    // 추가할 수량 입력
                    val editText = EditText(this@ManagerItemListActivity)
                    editText.gravity = Gravity.CENTER
                    editText.inputType = InputType.TYPE_CLASS_NUMBER
                    editText.hint = "추가할 재고"

                    // 다이얼로그 생성
                    AlertDialog.Builder(this@ManagerItemListActivity)
                            .setTitle("상품 재고 추가")
                            .setMessage("추가할 수량을 입력하세요")
                            .setView(editText)
                            .setPositiveButton("추가", object :DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    val count = editText.text.toString()
                                    val Count = count.toInt()
                                    service3.add_stock(listItems[position].productId, Count)
                                            .enqueue(object : Callback<AddStockResponse> {
                                                override fun onResponse(
                                                        call: Call<AddStockResponse>,
                                                        response: Response<AddStockResponse>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        val result = response.body()
                                                        Log.e("성공", "${result}")
                                                        Toast.makeText(this@ManagerItemListActivity, "재고 추가 완료", Toast.LENGTH_SHORT).show()

                                                        // 리사이클러뷰 갱신
                                                        val intent = Intent(this@ManagerItemListActivity, ManagerItemListActivity::class.java)
                                                        intent.putExtra("martId", martId)
                                                        startActivity(intent)
                                                        finish()
                                                    } else {
                                                        Log.d("재고 추가", "${Count}실패")
                                                    }
                                                }
                                                override fun onFailure(
                                                        call: Call<AddStockResponse>,
                                                        t: Throwable
                                                ) {
                                                    Log.e("연결실패", t.message.toString())
                                                }
                                            })
                                }
                            })
                            .setNegativeButton("취소", object : DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                }
                            })
                            .create()
                            .show()
                }
            })


            // 리사이클러뷰 롱클릭
            // 롱클릭시 제품 삭제

            ManagerItemListAdapter.setItemLongClickListener(object : ManagerItemListAdapter.OnItemLongClickListener{
                override fun onLongClick(v: View, position: Int) {
                    // 다이얼로그 띄우기
                    AlertDialog.Builder(this@ManagerItemListActivity)
                        .setTitle("마트 상품 삭제")
                        .setMessage("${listItems[position].productName}을(를) 마트 판매 상품에서 삭제하시겠습니까?")
                        .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                service2.delete_item(listItems[position].productId)
                                    .enqueue(object : Callback<DeleteProductResponse> {
                                        override fun onResponse(
                                            call: Call<DeleteProductResponse>,
                                            response: Response<DeleteProductResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                val result = response.body()
                                                Log.e("성공", "${result}")

                                                // 리사이클러뷰 갱신
                                                listItems.removeAt(position) // 리사이클러뷰에서도 삭제
                                                ManagerItemListAdapter.notifyDataSetChanged() // 리사이클러뷰 갱신
                                            } else {
                                                Log.d("장바구니 삭제", "실패")
                                            }
                                        }
                                        override fun onFailure(
                                            call: Call<DeleteProductResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("연결실패", t.message.toString())
                                        }
                                    })
                            }
                        })
                        .create()
                        .show()
                }
            })


        }
    }

    // 카테고리 별 아이템 보기
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

        val service = retrofit.create(MartItemService::class.java)

        service.search_category(categoryId, martId)
                .enqueue(object : Callback<MartCategoryResponse> {
                    override fun onResponse(
                            call: Call<MartCategoryResponse>,
                            response: Response<MartCategoryResponse>
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

                    override fun onFailure(call: Call<MartCategoryResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })


    }


    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: MartCategoryResponse?) {
        listItems.clear() // 리스트 초기화
        //결과 리스트 읽어오기
        for (productList in searchResult!!.productList) {
            listItems.add(productList)
        }
        ManagerItemListAdapter.notifyDataSetChanged()
    }
    }


interface MartItemService {
    @GET("search/mart/{martId}")
    fun search_item(
            @Path("martId") martId: Long
    ): Call<MartProductResponse>

    @GET("search/mart/category/{categoryId}/{martId}")
    fun search_category(
            @Path("categoryId") categoryId: Long,
            @Path("martId") martId: Long
    ): Call<MartCategoryResponse>
}

interface deleteProduct{
    @POST("mart/delete/{productId}")
    fun delete_item(
            @Path("productId") productId: Long
    ): Call<DeleteProductResponse>
}

interface AddStock{
    @POST("mart/{productId}/{count}")
    fun add_stock(
            @Path("productId") productId: Long,
            @Path("count") count: Int
    ): Call<AddStockResponse>
}