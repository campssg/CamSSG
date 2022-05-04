package com.example.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.Owner.AddItemOneActivity
import com.example.graduationproject.Owner.ManageItemModifyActivity
import com.example.graduationproject.Owner.SetMartActivity
import com.example.graduationproject.User.CampSearchActivity
import com.example.graduationproject.User.ChecklistCategoryActivity
import com.example.graduationproject.User.UserCartActivity
import com.example.graduationproject.User.UserMainActivity
import com.example.graduationproject.UserSigning.LoginActivity_User
import com.example.graduationproject.UserSigning.RegisterActivity_User
import com.example.graduationproject.UserSigning.SelectWhatToLoginActivity
import com.example.graduationproject.UserSigning.SelectWhatToRegisterActivity
import com.example.graduationproject.databinding.ActivityMainBinding

//페이지 테스트하는 곳

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        //회원가입
        binding.button1.setOnClickListener {
            val intent = Intent(this, SelectWhatToRegisterActivity::class.java)
            startActivity(intent)
        }
        //로그인
  binding.login.setOnClickListener {
            val intent = Intent(this, SelectWhatToLoginActivity::class.java)
            startActivity(intent)
        }
        //홈화면
        binding.button3.setOnClickListener {
            val intent = Intent(this, UserMainActivity::class.java)
            startActivity(intent)
        }


        //캠핑장 조회
        binding.button12.setOnClickListener {
            val intent = Intent(this, CampSearchActivity::class.java)
            startActivity(intent)
        }

        //장바구니
        binding.button11.setOnClickListener {
            val intent = Intent(this, UserCartActivity::class.java)
            startActivity(intent)
        }

        //주문내역
//        binding.button15.setOnClickListener {
//            val intent = Intent(this, OrderlistActivity::class.java)
//            startActivity(intent)
//        }
        binding.button15.setOnClickListener {
            val intent = Intent(this, ManageItemModifyActivity::class.java)
            startActivity(intent)
        }
        binding.button16.setOnClickListener {
            val intent = Intent(this, SetMartActivity::class.java)
            startActivity(intent)
        }

        binding.button17.setOnClickListener {
            val intent = Intent(this, AddItemOneActivity::class.java)
            startActivity(intent)
        }

        binding.button18.setOnClickListener {
            val intent = Intent(this, ChecklistCategoryActivity::class.java)
            startActivity(intent)
        }

        //요청상품 마트 버전

        binding.button19.setOnClickListener {
            val intent = Intent(this, ItemRequestActivity::class.java)
            startActivity(intent)
        }




    }
}