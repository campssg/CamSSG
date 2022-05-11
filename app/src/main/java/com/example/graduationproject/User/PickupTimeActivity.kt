package com.example.graduationproject.User

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Request.AddOrderRequest
import com.example.graduationproject.Api.Response.MartInfoResponse
import com.example.graduationproject.databinding.Activity1pickupTimeBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.graduationproject.Api.Response.AddOrderResponse

class PickupTimeActivity : AppCompatActivity() {
    private lateinit var binding : Activity1pickupTimeBinding
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        val martId = intent.getLongExtra("martId", 0)

        super.onCreate(savedInstanceState)
        binding = Activity1pickupTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 선택한 마트 정보 가져오기
        val martname = binding.martName
        val martnum = binding.martNum
        val martadr = binding.martAddress

        var reservedDate = ""
        var reservedTime = ""

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

        val service = retrofit.create(MartInfo::class.java)
        service.get_martInfo(martId)
            .enqueue(object : Callback<MartInfoResponse> {
                override fun onResponse(
                    call: Call<MartInfoResponse>,
                    response: Response<MartInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()

                        martname.text = result?.martName
                        martnum.text = result?.martNumber
                        martadr.text = result?.martAddress
                        if (result?.requestYn == 1) {
                            binding.requestBtn.setText("물품 요청 불가 매장")
                            binding.requestBtn.isEnabled = false
                        }
                    } else {
                        Log.d("마트 정보 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<MartInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })

        // 날짜 설정
        val settingDate = binding.calendarSet

        val spinner1 = binding.timePick

        spinner1.adapter = ArrayAdapter.createFromResource(this, com.example.graduationproject.R.array.pu_time_array, android.R.layout.simple_spinner_item)
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                reservedTime = spinner1.selectedItem.toString()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                    1 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                    2 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                    3 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                    4 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                    5 -> {
                        reservedTime = spinner1.selectedItem.toString()
                    }
                }
            }
        }

        settingDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var mon = ""
                    var day = ""
                    day = if (dayOfMonth < 10) {
                        "0${dayOfMonth}"
                    } else {
                        "${dayOfMonth}"
                    }

                    mon = if (month + 1 < 10) {
                        "0${month+1}"
                    } else {
                        "${month+1}"
                    }

                    reservedDate = "${year}-"+ mon + "-" + day

                    binding.setDate.setText("${year} - "+ mon + " - " + day)
                }
            }, year, month, date)

            dlg.show()
        }

        binding.requestBtn.setOnClickListener {
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

            val service = retrofit.create(AddOrder::class.java)
            var data = AddOrderRequest(reservedDate, reservedTime)

            AlertDialog.Builder(this@PickupTimeActivity)
                .setTitle("주문서 등록")
                .setMessage("현재 픽업 날짜와 시간을 확정하시겠습니까? 물품 요청 페이지 이후엔, 변경이 불가능합니다.")
                .setPositiveButton("예", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        service.addOrder(data)
                            .enqueue(object : Callback<AddOrderResponse> {
                                override fun onResponse(
                                    call: Call<AddOrderResponse>,
                                    response: Response<AddOrderResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val result = response.body()

                                        var intent = Intent(this@PickupTimeActivity, AddRequestProductActivity::class.java)
                                        intent.putExtra("orderId", result?.orderId)
                                        startActivity(intent)
                                    } else {
                                        Log.d("주문서 생성", "실패")
                                    }
                                }

                                override fun onFailure(call: Call<AddOrderResponse>, t: Throwable) {
                                    Log.e("연결실패", t.message.toString())
                                }
                            })
                    }
                })
                .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        finish()
                    }
                })
                .create()
                .show()
        }

        binding.paymentBtn.setOnClickListener {
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

            val service = retrofit.create(AddOrder::class.java)
            var data = AddOrderRequest(reservedDate, reservedTime)

            service.addOrder(data)
                .enqueue(object : Callback<AddOrderResponse> {
                    override fun onResponse(
                        call: Call<AddOrderResponse>,
                        response: Response<AddOrderResponse>
                    ) {
                        Log.d("주문서 생성", "성공")
                        if (response.isSuccessful) {
                            val result = response.body()
                            print(result)
                            Toast.makeText(this@PickupTimeActivity, "주문서 생성을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                            var outintent = Intent(this@PickupTimeActivity, HJ_UserPaymentActivity::class.java)
                            outintent.putExtra("orderId", result?.orderId.toString())
                            outintent.putExtra("phoneNumber", result?.order_phoneNumber)
                            outintent.putExtra("martName", result?.martName)
                            outintent.putExtra("pickup_day", result?.pickup_day)
                            outintent.putExtra("pickup_time", result?.pickup_time)
                            outintent.putExtra("name", result?.userName)
                            outintent.putExtra("total_price", result?.totalPrice)
                            startActivity(outintent)
                            finish()
                        } else {
                            Log.d("주문서 생성", "실패")
                        }
                    }

                    override fun onFailure(call: Call<AddOrderResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }
    }

    interface MartInfo {
        @GET("user/mart/{martId}")
        fun get_martInfo(
            @Path("martId") martId: Long
        ): Call<MartInfoResponse>
    }

    interface AddOrder {
        @POST("order/add")
        @Headers("content-type: application/json", "accept: application/json")
        fun addOrder(
            @Body request: AddOrderRequest
        ): Call<AddOrderResponse>
    }
}