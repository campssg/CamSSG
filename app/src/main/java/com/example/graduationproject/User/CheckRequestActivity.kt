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
import com.example.graduationproject.Adapter.RequestProductAdapter
import com.example.graduationproject.Api.Response.RequestPriceResponse
import com.example.graduationproject.Api.Response.RequestProductResponse
import com.example.graduationproject.Api.Response.SearchMartCategoryResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1checkRequestBinding
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

class CheckRequestActivity : AppCompatActivity() {
    private lateinit var binding : Activity1checkRequestBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<RequestProductResponse>()
    val requestProductAdapter = RequestProductAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1checkRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList1CheckRequest.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList1CheckRequest.adapter = requestProductAdapter

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

        val service = retrofit.create(RequestProductService::class.java)

        // 스피너 설정
        val spinner = binding.requestSpinner
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.item_request, android.R.layout.simple_spinner_item)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                getRequestItem()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> {
                        getRequestItem()
                    }
                    1 -> {
                        service.get_price()
                            .enqueue(object : Callback<List<RequestProductResponse>> {
                                override fun onResponse(
                                    call: Call<List<RequestProductResponse>>,
                                    response: Response<List<RequestProductResponse>>
                                ) {
                                    if (response.isSuccessful) {
                                        val result = response.body()
                                        Log.e("조회 완료", "${result}")

                                        // 리사이클러뷰에 결과 출력 요청 함수
                                        AddItemToList(result!!)
                                    } else {
                                        Log.d("요청 상품 조회", "실패")
                                    }
                                }

                                override fun onFailure(call: Call<List<RequestProductResponse>>, t: Throwable) {
                                    Log.e("연결실패", t.message.toString())
                                }
                            })
                    }
                }
            }
        }

        // 리사이클러뷰 클릭 이벤트
        requestProductAdapter.setItemClickListener(object : RequestProductAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 수량 입력 받을 EditText 생성
                val editText = EditText(this@CheckRequestActivity)
                editText.gravity = Gravity.CENTER
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.hint = "요청할 가격 입력"

                if (listItems[position].requestedProductState.equals("흥정완료")) {
                    Toast.makeText(this@CheckRequestActivity, "흥정이 완료된 상품입니다.", Toast.LENGTH_SHORT).show()
                } else if (listItems[position].requestedProductState.equals("가격요청중")) {
                    Toast.makeText(this@CheckRequestActivity, "이미 가격을 요청한 상품입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this@CheckRequestActivity)
                        .setTitle("가격이 제시되었습니다")
                        .setMessage("가격을 승인하시거나 새로 요청하실 수 있습니다")
                        .setPositiveButton("승인", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                service.request_accept(listItems[position].requestedProductId)
                                    .enqueue(object : Callback<RequestPriceResponse> {
                                        override fun onResponse(
                                            call: Call<RequestPriceResponse>,
                                            response: Response<RequestPriceResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                val result = response.body()
                                                Log.e("성공", "${result}")
                                                Toast.makeText(this@CheckRequestActivity, "승인이 완료되었습니다", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this@CheckRequestActivity, CheckRequestActivity::class.java))
                                                finish()
                                            } else {
                                                Log.d("승인", "실패")
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<RequestPriceResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("연결실패", t.message.toString())
                                        }
                                    })
                            }
                        })
                        .setNegativeButton("가격 요청", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                AlertDialog.Builder(this@CheckRequestActivity)
                                    .setTitle("가격 요청")
                                    .setMessage("새로 요청하실 총 가격을 입력해주세요")
                                    .setView(editText)
                                    .setPositiveButton("요청", object : DialogInterface.OnClickListener {
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            val price = editText.text.toString().toInt()
                                            service.request_price(listItems[position].requestedProductId, price)
                                                .enqueue(object : Callback<RequestPriceResponse> {
                                                    override fun onResponse(
                                                        call: Call<RequestPriceResponse>,
                                                        response: Response<RequestPriceResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val result = response.body()
                                                            Log.e("성공", "${result}")
                                                            Toast.makeText(this@CheckRequestActivity, "가격 요청이 완료되었습니다", Toast.LENGTH_SHORT).show()
                                                            startActivity(Intent(this@CheckRequestActivity, CheckRequestActivity::class.java))
                                                            finish()
                                                        } else {
                                                            Log.d("가격 요청", "실패")
                                                        }
                                                    }
                                                    override fun onFailure(
                                                        call: Call<RequestPriceResponse>,
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
                        .create()
                        .show()
                }
            }
        })
    }

    private fun getRequestItem() {
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

        val service = retrofit.create(RequestProductService::class.java)

        service.get_total()
            .enqueue(object : Callback<List<RequestProductResponse>> {
                override fun onResponse(
                    call: Call<List<RequestProductResponse>>,
                    response: Response<List<RequestProductResponse>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")

                        // 리사이클러뷰에 결과 출력 요청 함수
                        AddItemToList(result!!)
                    } else {
                        Log.d("요청 상품 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<List<RequestProductResponse>>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })
    }

    // 검색 결과 받아와서 리사이클러뷰에 추가
    private fun AddItemToList(searchResult: List<RequestProductResponse>) {
        listItems.clear() // 리스트 초기화
        //결과 리스트 읽어오기
        for (productList in searchResult) {
            listItems.add(productList)
        }
        requestProductAdapter.notifyDataSetChanged()
    }
}

interface RequestProductService {
    @GET("request/total/guest")
    fun get_total(): Call<List<RequestProductResponse>>

    @GET("request/guest")
    fun get_price(): Call<List<RequestProductResponse>>

    @POST("request/price/guest/{requestedProductId}/{price}")
    fun request_price(
        @Path("requestedProductId") requestedProductId: Long,
        @Path("price") price: Int
    ): Call<RequestPriceResponse>

    @POST("request/accept/{requestedProductId}")
    fun request_accept(
        @Path("requestedProductId") requestedProductId: Long
    ): Call<RequestPriceResponse>
}