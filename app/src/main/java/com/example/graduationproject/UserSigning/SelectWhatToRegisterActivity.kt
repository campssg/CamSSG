package com.example.graduationproject.UserSigning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.databinding.ActivitySelectWhatToRegisterBinding

class SelectWhatToRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectWhatToRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectWhatToRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        //사용자 회원가입
        binding.RegisterInUser.setOnClickListener {
            val intent = Intent(this, RegisterActivity_User::class.java)
            startActivity(intent)
        }

        //마트 회원가입
        binding.RegisterInMart.setOnClickListener {
            val intent = Intent(this, RegisterActivity_Mart::class.java)
            startActivity(intent)
        }

    }
}