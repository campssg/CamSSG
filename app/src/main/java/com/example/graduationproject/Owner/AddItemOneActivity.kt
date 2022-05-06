package com.example.graduationproject.Owner

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Response.AddItemOneResponse
import com.example.graduationproject.R
import com.example.graduationproject.User.AddHeaderJWT
import com.example.graduationproject.databinding.ActivityAddItemOneBinding
import kotlinx.android.synthetic.main.activity_add_item_one.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.util.concurrent.TimeUnit

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

        var category : Long = 0

        // 스피너 설정
        val spinner = binding.itemCategory
        ArrayAdapter.createFromResource(
            this, R.array.category_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    1 -> {
                        category = 1
                    }
                    2 -> {
                        category = 2
                    }
                    3 -> {
                        category = 3
                    }
                    4 -> {
                        category = 4
                    }
                    5 -> {
                        category = 5
                    }
                    6 -> {
                        category = 6
                    }
                    7 -> {
                        category = 7
                    }
                    8 -> {
                        category = 8
                    }
                    9 -> {
                        category = 9
                    }
                }
            }
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

        val service = retrofit.create(AddItemOneService::class.java)

        // 인탠트 해왔을 때 마트 아이디 받아오기
        val martId = intent.getLongExtra("martId", 0)

        binding.itemSubmit.setOnClickListener {
            val productName = binding.itemNameEdit.text.toString()
            val productStock = binding.itemNumEdit.text.toString()
            val productPrice = binding.itemPriceEdit.text.toString()
            val categoryId = category

            // 이미지 파일 생성
            val file = File(absolutePath(photoUri!!))
            val requestBody : RequestBody = file.asRequestBody("image/*".toMediaType())
            val uploadImg : MultipartBody.Part = MultipartBody.Part.createFormData("img", file.name, requestBody)

            // API 호출
            service.add_item(martId, categoryId, productName = productName,
                productPrice = productPrice.toInt(), productStock = productStock.toInt(), uploadImg)
                .enqueue(object : Callback<AddItemOneResponse> {
                    override fun onResponse(
                        call: Call<AddItemOneResponse>,
                        response: Response<AddItemOneResponse>
                    ) {
                        println(response)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.e("등록 완료", "${result}")

                            AlertDialog.Builder(this@AddItemOneActivity)
                                .setTitle("상품 등록 완료")
                                .setMessage("${productName} ${productStock}개를 마트에 등록했습니다.\n추가로 등록하시겠습니까?")
                                .setPositiveButton("예", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                    }
                                })
                                .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        startActivity(Intent(this@AddItemOneActivity, OwnerMainActivity::class.java))
                                        finish()
                                    }
                                })
                                .create()
                                .show()
                        } else {
                            Log.d("마트 상품 등록", "실패")
                        }
                    }

                    override fun onFailure(call: Call<AddItemOneResponse>, t: Throwable) {
                        Log.e("연결실패", t.message.toString())
                    }
                })
        }

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
                (Toast.makeText(this, photoUri.toString(), Toast.LENGTH_SHORT)).show()
            }
        }
    }

    private fun absolutePath(uri: Uri) : String {
        val proj : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = contentResolver.query(uri, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c!!.moveToFirst()
        val result = c.getString(index!!)
        return result
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
        @Part img: MultipartBody.Part?
    ): Call<AddItemOneResponse>
}