package com.example.graduationproject

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1cheapestcategoryBinding

class CheapestCategoryActivity: AppCompatActivity() {
    private lateinit var binding: Activity1cheapestcategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1cheapestcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

    }
}