package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.Api.Response.GetMartInfoResponse
import com.example.graduationproject.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.User.OrderlistActivity
import com.example.graduationproject.UserSigning.SelectWhatToLoginActivity
import com.example.graduationproject.databinding.ActivityOwnermainBinding
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class OwnerMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityOwnermainBinding

    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnermainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = pref.getString("userName", "")

        binding.omainUsername.setText(userName)

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

        val service = retrofit.create(GetMartService::class.java)

       service.get_mart()
           .enqueue(object : Callback<GetMartInfoResponse> {
               override fun onResponse(
                   call: Call<GetMartInfoResponse>,
                   response: Response<GetMartInfoResponse>
               ) {
                   if(response.isSuccessful) {
                       val result = response.body()
                       Log.e("조회 완료", "${result}")
                       if (result?.data.isNullOrEmpty()) {
                           binding.setmartBtn.visibility = View.VISIBLE
                       } else {
                           binding.setmartBtn.visibility = View.INVISIBLE
                       }
                   } else {
                       Log.d("마트 조회", "실패")
                   }
               }

               override fun onFailure(call: Call<GetMartInfoResponse>, t: Throwable) {
                   Log.e("연결실패", t.message.toString())
               }
           })


        // 햄버거 클릭시 메뉴
        binding.omainMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(Gravity.LEFT)
        }

        //마트 등록을 요구하는 버튼을 누를시 등록화면으로 이동
        binding.setmartBtn.setOnClickListener {
            val intent = Intent(this, SetMartActivity::class.java)
            startActivity(intent)
        }


        //상품 등록
        binding.omainSetP.setOnClickListener {
            val intent = Intent(this, MartListActivity::class.java)
            intent.putExtra("menu", 1)
            startActivity(intent)
        }

        //상품 관리
        binding.omainManageP.setOnClickListener {
            val intent = Intent(this, MartListActivity::class.java)
            intent.putExtra("menu", 2)
            startActivity(intent)
        }

        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
    }

    // 메뉴 선택
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.set_mart->startActivity(Intent(this, SetMartActivity::class.java))
            R.id.item_add->{
                val intent = Intent(this, MartListActivity::class.java)
                intent.putExtra("menu", 1)
                startActivity(intent)
            }
            R.id.item_manage->{
                val intent = Intent(this, MartListActivity::class.java)
                intent.putExtra("menu", 2)
                startActivity(intent)
            }
            R.id.price_deal->{
                val intent = Intent(this, MartListActivity::class.java)
                intent.putExtra("menu", 3)
                startActivity(intent)
            }
            R.id.owner_mypage1->startActivity(Intent(this, OwnerInfoActivity::class.java))
            R.id.owner_mypage2->{
                val intent = Intent(this, MartListActivity::class.java)
                intent.putExtra("menu", 4)
                startActivity(intent)
            }
            R.id.owner_mypage3-> {
                val intent = Intent(this, MartListActivity::class.java)
                intent.putExtra("menu", 5)
                startActivity(intent)
            }
            // 회원 탈퇴  R.id.owner_mypage4->startActivity(Intent(this, ::class.java))
            R.id.owner_mypage5->startActivity(Intent(this, SelectWhatToLoginActivity::class.java))
        }
        return false
    }
}

interface GetMartService {
    @GET("mart/info")
    fun get_mart():Call<GetMartInfoResponse>
}