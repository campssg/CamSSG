package com.example.graduationproject.UserSigning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.databinding.ActivitySelectWhatToLoginBinding

class SelectWhatToLoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySelectWhatToLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySelectWhatToLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        //사용자 로그인
        binding.LoginInUser.setOnClickListener {
            val intent = Intent(this, LoginActivity_User::class.java)
            startActivity(intent)
        }

        //마트 로그인
        binding.LoginInMart.setOnClickListener {
            val intent = Intent(this, LoginActivity_Mart::class.java)
            startActivity(intent)
        }


    }
}