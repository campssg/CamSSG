package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1categoryItemListBinding

class UserCategoryItemList : AppCompatActivity() {
    private lateinit var binding : Activity1categoryItemListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1categoryItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val item_category = binding.itemCategory
        item_category.adapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item)

        // 장바구니 이미지 클릭 시
        binding.cartImg.setOnClickListener {
            startActivity(Intent(this, UserCartActivity::class.java))
        }

        //장바구니 글씨 클릭 시
        binding.cartTxt.setOnClickListener {
            startActivity(Intent(this, UserCartActivity::class.java))
        }
    }
}