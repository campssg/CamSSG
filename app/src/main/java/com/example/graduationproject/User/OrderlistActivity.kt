package com.example.graduationproject.User

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1orderlistBinding

//주문내역

class OrderlistActivity : AppCompatActivity() {
    private lateinit var binding : Activity1orderlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1orderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}


