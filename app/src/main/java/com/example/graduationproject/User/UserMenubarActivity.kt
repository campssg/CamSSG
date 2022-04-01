package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1menubarBinding

class UserMenubarActivity: AppCompatActivity() {
    private lateinit var binding: Activity1menubarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1menubarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        //캠핑장 조회
        binding.menuCampsearch.setOnClickListener {
            //val intent = Intent(this, )
            setContentView(R.layout.activity_1campsearch)
        }

        //캠핑장 즐겨찾기
        binding.menuCamplike.setOnClickListener {
            setContentView(R.layout.activity_1bookmark_camping)
        }

        //마트 조회
        binding.menuMartsearch.setOnClickListener {
            setContentView(R.layout.activity_1martsearch)
        }

        //마트 즐겨찾기
        binding.menuMartlike.setOnClickListener {
            setContentView(R.layout.activity_1bookmark_mart)
        }

        //장바구니
        binding.menuCart.setOnClickListener {
        }

        //주문내역
        binding.menuOrdered.setOnClickListener {
            setContentView(R.layout.activity_1orderlist)
        }

        //정보수정
        binding.menuMyinfo.setOnClickListener {
            setContentView(R.layout.activity_1mypage)
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }

        //탈퇴
        binding.menuBreak.setOnClickListener {

            //탈퇴 코드

        }

        //로그아웃
        binding.menuLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //로그아웃 코드

        }

    }
}