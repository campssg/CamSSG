package com.example.graduationproject.User

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.graduationproject.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.UserSigning.SelectWhatToLoginActivity
import com.example.graduationproject.databinding.Activity1bookmarkCampingBinding
import com.example.graduationproject.databinding.Activity1usermainBinding
import com.example.graduationproject.databinding.ActivityLoginBinding
import com.google.android.material.navigation.NavigationView

class UserMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: Activity1usermainBinding

    lateinit var navigationView: NavigationView

    var pressedTime = 0L //뒤로가기 버튼 클릭했을 때의 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1usermainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "")

        binding.umainUsername.setText(userName)

        // 햄버거 클릭시 좌측 메뉴
        binding.umainMenubar.setOnClickListener {
            binding.drawerlayout.openDrawer(Gravity.LEFT)
        }

        //주문내역
        binding.umainOrdered.setOnClickListener {
            val intent = Intent(this, OrderlistActivity::class.java)
            startActivity(intent)
        }

        //장바구니
        binding.umainBasket.setOnClickListener {
            val intent = Intent(this, UserCartActivity::class.java)
            startActivity(intent)
        }

        //캠핑장 조회
        binding.umainCampsearch.setOnClickListener {
            startActivity(Intent(this, CampSearchActivity::class.java))
        }

        // 마트 조회
        binding.umainMartsearch.setOnClickListener {
            startActivity(Intent(this, MartSearchActivity::class.java))
        }

        //내정보 수정
        binding.umainUserimg.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }

        // 가격 비교
        binding.umainOrder.setOnClickListener {
            startActivity(Intent(this, UserCategoryItemList::class.java))
        }


        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.user_camping1->startActivity(Intent(this, CampSearchActivity::class.java))
            R.id.user_camping2->startActivity(Intent(this, bookmark_campingActivity::class.java))
            R.id.user_mart1->startActivity(Intent(this, MartSearchActivity::class.java))
            R.id.user_mart2->startActivity(Intent(this, bookmark_martActivity::class.java))
            R.id.user_mypage1->startActivity(Intent(this, UserCartActivity::class.java))
            R.id.user_mypage2->startActivity(Intent(this, OrderlistActivity::class.java))
            R.id.user_mypage3->startActivity(Intent(this, UserInfoActivity::class.java))
            //R.id.user_mypage4->startActivity(Intent(this, CampSearchActivity::class.java))
            R.id.user_mypage5->startActivity(Intent(this, SelectWhatToLoginActivity::class.java))
            R.id.user_mart3->startActivity(Intent(this, CheckRequestActivity::class.java))
        }
        return false
    }

    //뒤로가기 2번 -> 앱 종료 이벤트
    override fun onBackPressed() {

        if(System.currentTimeMillis() - pressedTime >=1500){
            pressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else{
            // 모든 액티비티 종료
            finishAffinity()
            System.runFinalization()
            System.exit(0)
        }
    }
}