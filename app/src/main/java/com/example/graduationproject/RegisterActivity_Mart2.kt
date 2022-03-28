package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityRegistermart2Binding

class RegisterActivity_Mart2 : AppCompatActivity() {
    private lateinit var binding : ActivityRegistermart2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistermart2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

    }
}