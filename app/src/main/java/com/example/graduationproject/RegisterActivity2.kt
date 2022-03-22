package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityRegister2Binding

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityRegister2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

    }
}