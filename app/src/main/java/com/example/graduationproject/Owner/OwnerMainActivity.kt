package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.AddItemOneActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityOwnermainBinding

class OwnerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOwnermainBinding
    var martexist = 0   //마트 등록 여부 (0 -> 등록 안함, 1 -> 등록 함)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnermainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()


        //마트 등록을 요구하는 버튼을 누를시 등록화면으로 이동
        binding.setmartBtn.setOnClickListener {
            martexist = 1
            val intent = Intent(this, SetMartActivity::class.java)
            startActivity(intent)
        }

        //마트가 등록되어 있으면 등록을 요구하는 버튼이 안보이게 됨
        if(martexist != 0){
            binding.setmartBtn.setVisibility(View.GONE)
        }

        //상품 등록
        binding.omainSetP.setOnClickListener {
            val intent = Intent(this, AddItemOneActivity::class.java)
            startActivity(intent)
        }

        //상품 관리
        binding.omainManageP.setOnClickListener {
            val intent = Intent(this, ManageItemActivity::class.java)
            startActivity(intent)
        }
    }
}