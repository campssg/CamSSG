package com.example.graduationproject.Owner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityOwnermainBinding
import com.example.graduationproject.databinding.ActivitySetMartBinding

class SetMartActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetMartBinding
    private var photoUri: Uri? = null


    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetMartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "마트 등록"

        val spinner1 = binding.martTimeStart
        val spinner2 = binding.martTimeEnd

        spinner1.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)
        spinner2.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)

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

        //등록하기 누르면 메인화면으로
        binding.martInfoSubmit.setOnClickListener {
            Toast.makeText(this, "마트가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            var outintent = Intent(applicationContext, OwnerMainActivity::class.java)
            setResult(Activity.RESULT_OK, outintent)
            finish()
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