package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivitySelectWhatToRegisterBinding

class SelectWhatToRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectWhatToRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectWhatToRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
    }
}