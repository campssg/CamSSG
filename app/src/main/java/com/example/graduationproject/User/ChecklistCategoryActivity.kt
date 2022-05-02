package com.example.graduationproject.User

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Response.CategoryCheckListResponse
import com.example.graduationproject.Owner.ChecklistActivity
import com.example.graduationproject.UserSigning.RegisterActivity_User
import com.example.graduationproject.databinding.ActivityChecklistcategoryBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


//HJ 물품 체크리스트 카테고리 선택
class ChecklistCategoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChecklistcategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)



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




        //http://13.124.13.202:8080/api/v1/login
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.13.202:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()


        val service = retrofit.create(ChecklistCategory::class.java)

        binding.imgKimchi.setOnClickListener{


            service.Checklist(categoryId=2)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        //축산
        binding.imgMeat.setOnClickListener{


            val categoryId :Long = 1
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgDrinks.setOnClickListener{


            //생수
            val categoryId :Long = 3
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgSimple.setOnClickListener{

            //간편식품

            val categoryId :Long = 4
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgFruits.setOnClickListener{


            //과일/견과
            val categoryId :Long = 5
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgFresh.setOnClickListener{

            //채소

            val categoryId :Long = 6
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgProcessed.setOnClickListener{


            //가공식품
            val categoryId :Long = 7
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }

        binding.imgLiving.setOnClickListener{

            //생활용품

            val categoryId :Long = 8
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }


        binding.imgCamping.setOnClickListener{

            //가공식품

            val categoryId :Long = 9
            service.Checklist(categoryId)
                .enqueue(object: Callback<List<CategoryCheckListResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryCheckListResponse>>,
                        response: Response<List<CategoryCheckListResponse>>
                    ) {
                        val result = response.body()
                        Log.e("결과값","${result}")
                        if(response.isSuccessful){
                            val result = response.body()
                            Log.e("조회 완료","${result}")
                        }
                        else{
                            Log.e("카테고리 조회","실패")
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryCheckListResponse>>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }




    }
}


interface ChecklistCategory{

    @GET("checklist/{categoryId}")
    fun Checklist(
        @Path("categoryId") categoryId:Long
    ): Call<List<CategoryCheckListResponse>>
}