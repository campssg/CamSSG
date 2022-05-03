package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivitySelectAddOneOrManyBinding

class SelectAddOneOrManyActivity : AppCompatActivity () {
    private lateinit var binding: ActivitySelectAddOneOrManyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAddOneOrManyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        val martId = intent.getLongExtra("martId", 0)

        // 체크리스트로 일괄 등록하기
        binding.SelectAddMany.setOnClickListener {

        }

        // 하나만 등록하기
        binding.SelectAddOne.setOnClickListener {
            val intent = Intent(this, AddItemOneActivity::class.java)
            intent.putExtra("martId", martId)
            startActivity(intent)
        }
    }

}