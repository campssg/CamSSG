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
import com.example.graduationproject.Adapter.RequestProductAdapter
import com.example.graduationproject.Api.Response.RequestPriceResponse
import com.example.graduationproject.Api.Response.RequestProductResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.User.CheckRequestActivity
import com.example.graduationproject.databinding.ActivityItemRequestBinding
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


//요청상품 확인 페이지
class ItemRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemRequestBinding

    // 리사이클러뷰 어댑터 설정
    val listItems = arrayListOf<RequestProductResponse>()
    val requestProductAdapter = RequestProductAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val spinner = binding.RequestType
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.item_request,android.R.layout.simple_spinner_item)

        // 리사이클러뷰 레이아웃 매니저 설정, 어댑터 추가
        binding.rvList1CheckRequest.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList1CheckRequest.adapter = requestProductAdapter

        val martId = intent.getLongExtra("martId", 0)

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

        val service = retrofit.create(RequestProductService_mart::class.java)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                getRequestItem(martId)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> {
                        getRequestItem(martId)
                    }
                    1 -> {
                        service.get_price(martId)
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
                val editText = EditText(this@ItemRequestActivity)
                editText.gravity = Gravity.CENTER
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.hint = "제시할 가격 입력"

                if (listItems[position].requestedProductState.equals("흥정완료")) {
                    Toast.makeText(this@ItemRequestActivity, "흥정이 완료된 상품입니다.", Toast.LENGTH_SHORT).show()
                } else if (listItems[position].requestedProductState.equals("가격제시중")) {
                    Toast.makeText(this@ItemRequestActivity, "이미 가격을 제시한 상품입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this@ItemRequestActivity)
                        .setTitle("가격이 요청되었습니다")
                        .setMessage("가격을 승인하시거나 새로 제시하실 수 있습니다")
                        .setPositiveButton("승인", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                service.request_accept(listItems[position].requestedProductId)
                                    .enqueue(object : Callback<RequestPriceResponse> {
                                        override fun onResponse(
                                            call: Call<RequestPriceResponse>,
                                            response: Response<RequestPriceResponse>
                                        ) {
                                            println(response)
                                            if (response.isSuccessful) {
                                                val result = response.body()
                                                Log.e("성공", "${result}")
                                                Toast.makeText(this@ItemRequestActivity, "승인이 완료되었습니다", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this@ItemRequestActivity, ItemRequestActivity::class.java)
                                                intent.putExtra("martId", martId)
                                                startActivity(intent)
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
                        .setNegativeButton("가격 제시", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                AlertDialog.Builder(this@ItemRequestActivity)
                                    .setTitle("가격 제시")
                                    .setMessage("새로 제시하실 총 가격을 입력해주세요")
                                    .setView(editText)
                                    .setPositiveButton("제시", object : DialogInterface.OnClickListener {
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
                                                            Toast.makeText(this@ItemRequestActivity, "가격 제시가 완료되었습니다", Toast.LENGTH_SHORT).show()
                                                            listItems.removeAt(position)
                                                            requestProductAdapter.notifyDataSetChanged()
                                                        } else {
                                                            Log.d("가격 제시", "실패")
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
    private fun getRequestItem(martId: Long) {
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

        val service = retrofit.create(RequestProductService_mart::class.java)

        service.get_total(martId)
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

interface RequestProductService_mart {
    @GET("request/total/{martId}")
    fun get_total(
        @Path("martId") martId: Long
    ): Call<List<RequestProductResponse>>

    @GET("request/mart/{martId}")
    fun get_price(
        @Path("martId") martId: Long
    ): Call<List<RequestProductResponse>>

    @POST("request/price/mart/{requestedProductId}/{price}")
    fun request_price(
        @Path("requestedProductId") requestedProductId: Long,
        @Path("price") price: Int
    ): Call<RequestPriceResponse>

    @POST("request/accept/{requestedProductId}")
    fun request_accept(
        @Path("requestedProductId") requestedProductId: Long
    ): Call<RequestPriceResponse>
}

