package com.example.graduationproject

import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.databinding.Activity1compareCartListBinding
import retrofit2.Call
import retrofit2.http.GET

class CompareCartListActivity : AppCompatActivity() {
    private lateinit var binding : Activity1compareCartListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1compare_cart_list)
        binding = Activity1compareCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


    }

    private fun getLocation(){
        // val LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    interface CompareCart {
        @GET("api/v1/cart/{latitude}/{longitude}")
        fun get_compareCart(): Call<CompareCartResponse>
    }

}