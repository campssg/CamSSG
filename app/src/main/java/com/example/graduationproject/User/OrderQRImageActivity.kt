package com.example.graduationproject.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.Activity1qrimageBinding



//hj 사용자 주문 QR 이미지 발급

class OrderQRImageActivity : AppCompatActivity() {
    private lateinit var binding: Activity1qrimageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1qrimageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}