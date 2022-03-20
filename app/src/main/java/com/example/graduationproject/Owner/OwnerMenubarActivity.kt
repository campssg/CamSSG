package com.example.graduationproject.Owner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMenubarBinding

class OwnerMenubarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenubarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenubarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        //마트등록
        binding.menuMartRegist.setOnClickListener {
            setContentView(R.layout.activity_set_mart)
        }

        //상품등록
        binding.menuAddItem.setOnClickListener {
            setContentView(R.layout.activity_add_item_one)
        }

        //상품관리
        binding.menuManageItem.setOnClickListener {
            setContentView(R.layout.activity_manage_item_main)
        }

        //정보수정
        binding.menuMyinfo.setOnClickListener {
            setContentView(R.layout.activity_mypage)
        }

        //마트정보수정
        binding.menuMartinfo.setOnClickListener {
            setContentView(R.layout.activity_martinfo)
        }

        //주문현황
        binding.menuOrderedNow.setOnClickListener {
            setContentView(R.layout.activity_1orderlist)
        }

        //탈퇴
        binding.menuBreak.setOnClickListener {

            //탈퇴 코드

        }

        //로그아웃
        binding.menuLogout.setOnClickListener {

            //로그아웃 코드

        }
}