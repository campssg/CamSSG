package com.example.graduationproject.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Adapter.SearchMartItemListAdapter
import com.example.graduationproject.Api.Request.AddCartRequest
import com.example.graduationproject.Api.Response.*
import com.example.graduationproject.databinding.Activity1compareCartListDetailBinding
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserCompareCartDetail : AppCompatActivity() {
    private lateinit var binding : Activity1compareCartListDetailBinding
    private var canAdd = 1

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<ProductList>()
    val searchMartItemListAdapter = SearchMartItemListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1compareCartListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = searchMartItemListAdapter

        // 장바구니로 클릭시
        binding.cartBtn.setOnClickListener {
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

        // 인탠트해 올 때 받아옴
        val martId = intent.getLongExtra("martId", 0)
        val martName = intent.getStringExtra("martName")
        val martAddress = intent.getStringExtra("martAddress")
        val closeTime = intent.getStringExtra("closeTime")

        binding.martName.setText(martName)
        binding.martAddress.setText(martAddress)
        binding.martTime.setText(closeTime)

        service2.can_add(martId)
            .enqueue(object : Callback<CanAddResponse> {
                override fun onResponse(
                    call: Call<CanAddResponse>,
                    response: Response<CanAddResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        canAdd = result?.canAdd!!.toInt()
                    } else {
                        Log.d("조회", "실패")
                    }
                }

                override fun onFailure(call: Call<CanAddResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })

        // 리사이클러뷰 클릭 이벤트
        searchMartItemListAdapter.setItemClickListener(object: SearchMartItemListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 수량 입력 받을 EditText 생성
                val editText = EditText(this@UserCompareCartDetail)
                editText.gravity = Gravity.CENTER
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.hint = "수량 입력"

                // 다이얼로그 생성
                AlertDialog.Builder(this@UserCompareCartDetail)
                    .setTitle("장바구니 담기")
                    .setMessage("담으실 수량을 입력해주세요")
                    .setView(editText)
                    .setPositiveButton("추가", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val count = editText.text.toString()
                            val data = AddCartRequest(count.toInt())
                            if (canAdd == 0) {
                                AlertDialog.Builder(this@UserCompareCartDetail)
                                    .setTitle("다른 마트의 상품을 한 장바구니에 추가할 수 없습니다")
                                    .setMessage("기존 장바구니에 담겨있던 상품은 모두 삭제됩니다. 추가하시겠습니까?")
                                    .setPositiveButton("담기", object : DialogInterface.OnClickListener {
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            service2.add_new_item(listItems[position].productId, data)
                                                .enqueue(object : Callback<AddCartResponse> {
                                                    override fun onResponse(
                                                        call: Call<AddCartResponse>,
                                                        response: Response<AddCartResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val result = response.body()
                                                            Log.e("성공", "${result}")
                                                            canAdd = 1
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
                            } else {
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