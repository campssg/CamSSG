package com.example.graduationproject.Owner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Request.MartEditRequest
import com.example.graduationproject.Api.Response.MartListResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityMartinfoBinding
import com.example.graduationproject.databinding.ActivitySetMartBinding
import kotlinx.android.synthetic.main.activity_1bookmark_mart.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import java.util.concurrent.TimeUnit

//마트 정보 수정

class EditMartActivity :AppCompatActivity(){
    private lateinit var binding: ActivityMartinfoBinding
    private var photoUri: Uri? = null


    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMartinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인탠트해와서 데이터 반영
        val martId = intent.getLongExtra("martId", 0)
        var martName = intent.getStringExtra("martName");
        var martAddress = intent.getStringExtra("martAddress")
        var openTime = intent.getStringExtra("openTime")
        var closeTime = intent.getStringExtra("closeTime")
        var requestYn = intent.getLongExtra("requestYn", 0).toInt()

        binding.martNameEdit.setText(martName)
        binding.martAddressEdit.setText(martAddress)
        binding.martTimeStart.setText(openTime)
        binding.martTimeEnd.setText(closeTime)
        binding.martAddressEdit.inputType = InputType.TYPE_NULL
        if (requestYn == 1) {
            binding.martRequestYes.toggle()
        } else {
            binding.martRequestNo.toggle()
        }

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

        val service = retrofit.create(EditMartService::class.java)

        binding.martInfoSubmit.setOnClickListener {
            martName = binding.martNameEdit.text.toString()
            openTime = binding.martTimeStart.text.toString()
            closeTime = binding.martTimeEnd.text.toString()
            if (binding.martRequestYes.isChecked) {
                requestYn = 1
            } else {
                requestYn = 0
            }
            val data = MartEditRequest(martId, martName.toString(), openTime.toString(), closeTime.toString(), requestYn.toLong())
            println(data)
            service.edit_mart(data)
                .enqueue(object : Callback<MartListResponse> {
                    override fun onResponse(
                        call: Call<MartListResponse>,
                        response: Response<MartListResponse>
                    ) {
                        println(response)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("마트 수정 완료","${result}")

                            Toast.makeText(this@EditMartActivity, "마트 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            val outintent = Intent(applicationContext, OwnerMainActivity::class.java)
                            startActivity(outintent)
                            finish()
                        } else {
                            Log.d("마트 수정","실패")
                        }
                    }

                    override fun onFailure(call: Call<MartListResponse>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }
        
        binding.martimgSelect.setOnClickListener {
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
                Glide.with(this).load(photoUri).into(binding.martImg)

            }
        }

    }
}

interface EditMartService {
    @PATCH("mart/edit")
    fun edit_mart(
        @Body request : MartEditRequest
    ): Call<MartListResponse>
}