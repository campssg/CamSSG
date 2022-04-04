//package com.example.graduationproject.Api.api
//
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.graduationproject.User.AddHeaderJWT
//import com.example.graduationproject.User.CampList
//import com.example.graduationproject.User.CampListService
//import okhttp3.OkHttpClient
//import okhttp3.internal.http.hasBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
////
////
//////api 호출 안정되면 옮길 예정
//class CallApi {
//    val sharedPreferences = getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
//    val jwt = sharedPreferences.getString("jwt", "")
//
//    val client = OkHttpClient.Builder()
//        .addInterceptor(AddHeaderJWT(jwt.toString())) // JWT header 달아주는 interceptor 추가
//        .connectTimeout(1, TimeUnit.MINUTES)
//        .readTimeout(10, TimeUnit.SECONDS)
//        .writeTimeout(10, TimeUnit.SECONDS).build()
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://13.124.13.202:8080/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client).build()
//
//    //                 API 호출(캠핑장 결과)
//    fun CampList(status: String, callback: (Boolean, String, List<CampList>?) -> Unit) {
//        val service2 = retrofit.create(CampListService::class.java)
//
//
//        service2.search_result("Bearer"+"${}",
//            body)
//            .enqueue(object : Callback<CampList> {
//                override fun onResponse(
//                    call: Call<CampList>,
//                    response: retrofit2.Response<CampList>
//                ) {
//                    if (response.isSuccessful) {
//                        val result = response.body()
//                        Log.e("조회 완료", "${result}")
//                        Toast.makeText(
//                            this@CampSearchActivity,
//                            "번호 " + result?.tel + "개 조회 성공" + "첫번째 결과 :" + result?.campName,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
////                            binding.rvList.layoutManager = LinearLayoutManager(this)
////                            binding.rvList.setHasFixedSize(true)
//
//
//                    } else {
//                        Log.d("캠핑장 조회", "실패")
//
//                    }
//                }
//
//                override fun onFailure(call: Call<CampList>, t: Throwable) {
//                    Log.e("연결실패", t.message.toString())
//                }
//            })
//    }
//}