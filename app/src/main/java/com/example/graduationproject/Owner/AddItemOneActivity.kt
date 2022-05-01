package com.example.graduationproject.Owner

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.databinding.ActivityAddItemOneBinding
import kotlinx.android.synthetic.main.activity_add_item_one.*
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class AddItemOneActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddItemOneBinding
    private var photoUri: Uri? = null


    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 인탠트 해왔을 때 마트 아이디 받아오기
        val martId = intent.getLongExtra("martId", 0)

        itemimg_select.setOnClickListener {
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
        binding.itemSubmit.setOnClickListener {
            val intent = Intent(this, OwnerMainActivity::class.java)
            startActivity(intent)
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
                Glide.with(this).load(photoUri).into(binding.itemImg)
            }
        }
    }
}

// 마트 상품 하나씩 직접 등록 API 호출
interface AddItemOneService {
    @Multipart
    @POST("mart/{martId}")
    fun add_item(
        @Path("martId") martId: Long,
        @Query("categoryId") categoryId: Long,
        @Query("productName") productName: String,
        @Query("productPrice") productPrice: Int,
        @Query("productStock") productStock: Int,
        @Query("img") img: MultipartBody.Part
    )
}