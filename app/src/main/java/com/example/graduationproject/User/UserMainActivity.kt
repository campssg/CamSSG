package com.example.graduationproject.User

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.MainActivity
import com.example.graduationproject.databinding.Activity1menubarBinding
import com.example.graduationproject.databinding.Activity1usermainBinding
import com.example.graduationproject.databinding.ActivityLoginBinding

class UserMainActivity : AppCompatActivity() {
    private lateinit var binding: Activity1usermainBinding
    private lateinit var binding2: Activity1menubarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1usermainBinding.inflate(layoutInflater)
        binding2 = Activity1menubarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        binding.umainMenubar.setOnClickListener {
            binding.drawerlayout.openDrawer(Gravity.LEFT)

        }

        binding2.menuLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.umainOrdered.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.umainCampsearch.setOnClickListener {
            Toast.makeText(this, "캠핑장 조회로 이동", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CampSearchActivity::class.java)
            startActivity(intent)
        }
    }
}