package com.example.graduationproject.Owner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Request.MartAddRequest
import com.example.graduationproject.Api.Request.MartAuthRequest
import com.example.graduationproject.Api.Response.AddMartResponse
import com.example.graduationproject.Api.Response.MartAuthResponse
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySetMartBinding
import com.example.graduationproject.Owner.AddressActivity.Companion.ADDRESS_REQUEST_CODE
import com.example.graduationproject.User.AddHeaderJWT
import kotlinx.android.synthetic.main.activity_set_mart.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class SetMartActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetMartBinding
    private var photoUri: Uri? = null

//    private val martName: EditText by lazy {
//        findViewById(R.id.mart_name)
//    }

    private val address: EditText by lazy {
        findViewById(R.id.mart_address_edit)
    }

//    private val bno: EditText by lazy {
//        findViewById(R.id.mart_bno)
//    }

//    private val startDt: EditText by lazy {
//        findViewById(R.id.mart_opendt)
//    }

//    private val martAuthBtn: Button by lazy {
//        findViewById(R.id.mart_auth_btn)
//    }

    private var canNextStep: Boolean = false
    private var lat: String? = null
    private var lon: String? = null
    private var martAddress: String? = null
    private var requestYn: String? = null

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

        val service = retrofit.create(AuthMart::class.java)

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

        binding.martAddressEdit.setOnClickListener {
            // 버튼을 누르면 AddressActivity를 띄워주는 부분
            Intent(this, AddressActivity::class.java).apply {
                startActivityForResult(this, ADDRESS_REQUEST_CODE)
            }
        }

        binding.requestYnGroup.setOnCheckedChangeListener{ radioGroup, i ->
            when(i){
                R.id.mart_request_yes -> requestYn = "0"
                R.id.mart_request_no -> requestYn = "1"
            }
        }

        binding.martAuthBtn.setOnClickListener {
            val bno = binding.martBnoEdit.text.toString()
            val startDt = binding.martOpendtEdit.text.toString()

            val data = MartAuthRequest(bno, startDt)
            println(data)
            service.authMart(data).enqueue(object : Callback<MartAuthResponse> {
                override fun onResponse(
                    call: Call<MartAuthResponse>,
                    response: Response<MartAuthResponse>
                ) {
                    println(response)
                    if (response.isSuccessful) {
                        binding.martAuthBtn.setText("인증 완료")
                        canNextStep = true
                    }
                }

                override fun onFailure(call: Call<MartAuthResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })

        }

        //등록하기 누르면 메인화면으로
        binding.martInfoSubmit.setOnClickListener {
            if (!canNextStep) {
                Toast.makeText(this@SetMartActivity, "사업자 인증 후 진행해주세요.", Toast.LENGTH_SHORT).show()
            }

            var martName = binding.martNameEdit.text.toString()
            var startDt = binding.martOpendtEdit.text.toString()
            var openTime = binding.martTimeStart.selectedItem.toString()
            var closeTime = binding.martTimeStart.selectedItem.toString()

            martAddress += " " + binding.martAddressEditDetail.text.toString()

            val service = retrofit.create(AddMart::class.java)

            val data = MartAddRequest(martName, lon.toString(), lat.toString(), startDt, martAddress.toString()
                , openTime, closeTime, requestYn.toString())

            service.addMart(data)
                .enqueue(object : Callback<AddMartResponse> {
                    override fun onResponse(call: Call<AddMartResponse>, response: Response<AddMartResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SetMartActivity, "마트를 정상적으로 등록하였습니다.", Toast.LENGTH_SHORT).show()

                            var outintent = Intent(applicationContext, OwnerMainActivity::class.java)
                            setResult(Activity.RESULT_OK, outintent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<AddMartResponse>, t: Throwable) {
                        Log.e("연결실패",t.message.toString())
                    }
                })
        }
    }

    interface AuthMart {
        @POST("mart/auth")
        @Headers("content-type: application/json", "accept: application/json")
        fun authMart(@Body request: MartAuthRequest): Call<MartAuthResponse>
    }

    interface AddMart {
        @POST("mart")
        @Headers("content-type: application/json", "accept: application/json")
        fun addMart(@Body request: MartAddRequest): Call<AddMartResponse>
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        val addressData = data?.extras?.getString("address")
        if (addressData != null) {
            lat = addressData.split("|")[0]
            lon = addressData.split("|")[1]
            martAddress = addressData.split("|")[2]
            address.setText(martAddress)
        }

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