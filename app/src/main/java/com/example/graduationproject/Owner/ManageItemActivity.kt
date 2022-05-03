package com.example.graduationproject.Owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.Api.Response.UserInfoResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.User.UserInfoActivity
import com.example.graduationproject.databinding.Activity1categoryItemListBinding
import com.example.graduationproject.databinding.ActivityManageItemMainBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// 삭제하고 그냥 바로 상품 목록으로 가게끔
class ManageItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManageItemMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageItemMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 상품 목록
        binding.imageView2.setOnClickListener {
            startActivity(Intent(this, ManagerItemListActivity::class.java))
        }

    }

}