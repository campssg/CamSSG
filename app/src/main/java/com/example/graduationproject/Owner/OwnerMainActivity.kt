package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.User.OrderlistActivity
import com.example.graduationproject.UserSigning.SelectWhatToLoginActivity
import com.example.graduationproject.databinding.ActivityOwnermainBinding
import com.google.android.material.navigation.NavigationView

class OwnerMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityOwnermainBinding
    var martexist = 0   //마트 등록 여부 (0 -> 등록 안함, 1 -> 등록 함)

    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnermainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        val sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "")

        binding.omainUsername.setText(userName)


        // 햄버거 클릭시 메뉴
        binding.omainMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(Gravity.LEFT)
        }

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

        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
    }

    // 메뉴 선택
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.set_mart->startActivity(Intent(this, SetMartActivity::class.java))
            R.id.item_add->startActivity(Intent(this, SelectAddOneOrManyActivity::class.java))
            R.id.item_manage->startActivity(Intent(this, ManageItemActivity::class.java))
            // 가격 흥정  R.id.price_deal->startActivity(Intent(this, SetMartActivity::class.java))
            R.id.owner_mypage1->startActivity(Intent(this, OwnerInfoActivity::class.java))
            R.id.owner_mypage2->startActivity(Intent(this, EditMartActivity::class.java))
            R.id.owner_mypage3->startActivity(Intent(this, Order_MartListActivity::class.java))
            // 회원 탈퇴  R.id.owner_mypage4->startActivity(Intent(this, ::class.java))
            R.id.owner_mypage5->startActivity(Intent(this, SelectWhatToLoginActivity::class.java))
        }
        return false
    }
}