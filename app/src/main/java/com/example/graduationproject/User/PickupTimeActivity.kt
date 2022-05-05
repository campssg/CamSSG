package com.example.graduationproject.User

import android.app.DatePickerDialog
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
import android.R
import android.view.View
import android.widget.Spinner


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

        val text = spinner1.selectedItem.toString()

        sharedPreferences.edit().putString("putime", text).apply()

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
            var data = AddOrderRequest(reservedDate, text)
            print(data)
            service.addOrder(data)
                .enqueue(object : Callback<MartInfoResponse> {
                    override fun onResponse(
                        call: Call<MartInfoResponse>,
                        response: Response<MartInfoResponse>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            print(result)

                        } else {
                            Log.d("주문서 생성", "실패")
                        }
                    }

                    override fun onFailure(call: Call<MartInfoResponse>, t: Throwable) {
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
        ): Call<MartInfoResponse>
    }
}