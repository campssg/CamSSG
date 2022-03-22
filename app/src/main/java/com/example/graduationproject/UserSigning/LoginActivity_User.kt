package com.example.graduationproject.UserSigning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.databinding.ActivityLoginBinding

//사용자 로그인
class LoginActivity_User : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
    }
}