package com.example.graduationproject.Owner

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityManageItemModifyBinding
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ManageItemModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageItemModifyBinding
    private var photoUri: Uri? = null


    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageItemModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val TAG:String = "ManagerItemModifyActivity"
        Log.e(TAG, "Log---Start:       ")

        /*val productImgUrl = intent.getStringExtra("productImgUrl")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productStock = intent.getIntExtra("productStock", 0)
        val productCategory = intent.getStringExtra("productCategory")
        val productId = intent.getLongExtra("productId", 0)


        binding.modifyCategory.setText(productCategory)
        binding.modifyName.setText(productName)
        binding.modifyPrice.setText(productPrice.toString())
        binding.nowStock.setText(productStock.toString())

        // - 버튼
        binding.modifyitemMinusBTN.setOnClickListener {
            val stock = binding.modifyStock.text.toString()
            val stock_num = stock.toInt()
            if(stock_num!=0){
                binding.modifyStock.setText((stock_num-1).toString())
            }
        }

        // + 버튼
        binding.modifyitemPlusBTN.setOnClickListener {
            val stock = binding.modifyStock.text.toString()
            val stock_num = stock.toInt()
            binding.modifyStock.setText((stock_num+1).toString())
        }*/



        // 로그인 후 저장해둔 JWT 토큰 가져오기
        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val jwt = sharedPreferences.getString("jwt", "")

        val client = OkHttpClient.Builder()
                .addInterceptor(AddHeaderJWT(jwt.toString())) // JWT header 달아주는 interceptor 추가
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://13.124.13.202:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()


        // 이미지 선택
        binding.manageSelectBtn.setOnClickListener {
            val albumInternet =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것)
            albumInternet.type = "image/*"
            // 선택할 파일의 타입을 지정(안드로이드 OS가 사전작업을 할 수 있도록)
            val mimeType = arrayOf("image/*")
            albumInternet.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            @Suppress("DEPRECATION")
            startActivityForResult(albumInternet, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체를 추출한다.
            photoUri = data?.data

            //사진 업로드
            if (photoUri != null) {
                Glide.with(this).load(photoUri).into(binding.manageImg)

            }
        }

    }
}