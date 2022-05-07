package com.example.graduationproject.User

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.Api.Response.UserDetailOrderListResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1qrimageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit


//hj 사용자 주문 QR 이미지 발급

class OrderQRImageActivity : AppCompatActivity() {


    private lateinit var binding: Activity1qrimageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1qrimageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        CoroutineScope(Dispatchers.Main).launch {
//            val bitmap = withContext(Dispatchers.IO){
//                ImageLoader.loadImage(courseImage)
//            }
//            binding.QRIamge.setImageBitmap(bitmap)
//        }

        val orderId = intent.getLongExtra("orderId", 0)
        //액션바 숨기기
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

        val service = retrofit.create(QRImage::class.java)

        service.showQR(orderId)
            .enqueue(object : Callback<UserDetailOrderListResponse> {


                override fun onResponse(
                    call: Call<UserDetailOrderListResponse>,
                    response: retrofit2.Response<UserDetailOrderListResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회 완료", "${result}")


                        //QR 이미지 띄우기

                            Glide.with(this@OrderQRImageActivity).load(result!!.qrcode_url)
                            .into(binding.QRIamge)



                    } else {
                        Log.d("조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserDetailOrderListResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())

                }
            })


    }

    interface QRImage {
        @GET("/order/{orderId}")
        fun showQR(
            @Path("orderId") orderId: Long
        ): Call<UserDetailOrderListResponse>
    }

}