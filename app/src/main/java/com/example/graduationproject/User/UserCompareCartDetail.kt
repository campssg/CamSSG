package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1compareCartListDetailBinding

class UserCompareCartDetail : AppCompatActivity() {
    private lateinit var binding : Activity1compareCartListDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1compareCartListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 장바구니로 클릭시
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, UserCartActivity::class.java))
        }
    }
}