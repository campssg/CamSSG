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

    val listItems = arrayListOf<CategoryCheckListResponse>()

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


            //김치

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","2")
            startActivity(intent)

        }

        //축산
        binding.imgMeat.setOnClickListener{

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","1")
            startActivity(intent)


        }

        binding.imgDrinks.setOnClickListener{


            //생수

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","3")
            startActivity(intent)

        }

        binding.imgSimple.setOnClickListener{

            //간편식품

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","4")
            startActivity(intent)

        }

        binding.imgFruits.setOnClickListener{
            //과일/견과

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","5")
            startActivity(intent)

        }

        binding.imgFresh.setOnClickListener{

            //채소
            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","6")
            startActivity(intent)

        }

        binding.imgProcessed.setOnClickListener{


            //가공식품
            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","7")
            startActivity(intent)

        }

        binding.imgLiving.setOnClickListener{

            //생활용품
            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","8")
            startActivity(intent)

        }


        binding.imgCamping.setOnClickListener{

            //가공식품

            val intent =Intent(this@ChecklistCategoryActivity,ChecklistActivity::class.java)
            intent.putExtra("categoryId","9")
            startActivity(intent)

        }

    }
}


interface ChecklistCategory{

    @GET("checklist/{categoryId}")
    fun Checklist(
        @Path("categoryId") categoryId:Long
    ): Call<List<CategoryCheckListResponse>>
}