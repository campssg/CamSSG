package com.example.graduationproject

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.Activity1selectCategoryBinding

class SelectCategoryActivity : AppCompatActivity() {
    private lateinit var binding: Activity1selectCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1selectCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()    }
}