package com.example.graduationproject.User

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.Api.Request.UserNickRequest
import com.example.graduationproject.Api.Response.UserImgUpdatedResponse
import com.example.graduationproject.Api.Response.UserInfoResponse
import com.example.graduationproject.databinding.Activity1mypageBinding
import kotlinx.android.synthetic.main.activity_1mypage.*
import kotlinx.android.synthetic.main.activity_martinfo.*
import okhttp3.MediaType.Companion.toMediaType
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


//서비스 이용자 마이페이지
class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: Activity1mypageBinding
    private var photoUri: Uri? = null


    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1mypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        binding.mypage1img.clipToOutline=true


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

        val service = retrofit.create(UserInfo::class.java)

        // 사용자 정보 조회 API 호출
        service.get_userInfo()
            .enqueue(object : Callback<UserInfoResponse>{
                override fun onResponse(
                    call: Call<UserInfoResponse>,
                    response: Response<UserInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("조회완료", "${result}")

                        val sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
                        sharedPreferences.edit().putString("userName", result?.userName).apply()

                        // 조회한 결과로 텍스트 설정
                        binding.mypage1nametxt.setText(result?.userName)
                        binding.mypage1emailtxt.setText(result?.userEmail)
                        if (result?.userNickname!=null) {
                            binding.mypage1nickedit.setText(result.userNickname)
                        }
                    } else {
                        Log.d("사용자 정보 조회", "실패")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.e("연결실패", t.message.toString())
                }
            })

        val retrofit2 = Retrofit.Builder()
                .baseUrl("http://13.124.13.202:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

        val service2 = retrofit2.create(UserNickChange::class.java)

        val nickchangeBtn = binding.mypage1nickbtn

        // 닉네임 수정
        nickchangeBtn.setOnClickListener {

            val userNick = binding.mypage1nickedit.text.toString()
            val data = UserNickRequest(userNick)

            service2.change_nickname(data).enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.e("수정 완료","${result}")
                        dialog("Ok")
                    } else {
                        Log.d("수정","실패")
                        Log.d("수정","${userNick}")
                        Toast.makeText(this@UserInfoActivity, "수정에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.e("연결실패",t.message.toString())
                }

            })

        }

        img_1selectbtn.setOnClickListener {
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

        // 임시로 롱클릭으로 설정해둠
        img_1selectbtn.setOnLongClickListener {
            val file = File(absolutePath(photoUri!!))
            val requestBody : RequestBody = file.asRequestBody("image/jpeg".toMediaType())
            val uploadImg : MultipartBody.Part = MultipartBody.Part.createFormData("images", file.name, requestBody)
            Toast.makeText(this,"${uploadImg.body}, ${uploadImg.headers}", Toast.LENGTH_SHORT).show()
            service.update_img(uploadImg)
                .enqueue(object : Callback<UserImgUpdatedResponse> {
                    override fun onResponse(
                        call: Call<UserImgUpdatedResponse>,
                        response: Response<UserImgUpdatedResponse>
                    ) {
                        println(response)
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.d("성공", "${result}")
                        } else {
                            Log.d("실패", ".")
                        }
                    }

                    override fun onFailure(call: Call<UserImgUpdatedResponse>, t: Throwable) {
                        Log.e("실패", t.message.toString())
                    }
                })

            return@setOnLongClickListener true
        }
        // 비밀번호 변경
        binding.mypage1pass.setOnClickListener {
            val intent = Intent(this, UserPassChangeActivity::class.java)
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
                Glide.with(this).load(photoUri).into(binding.mypage1img)

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

    interface UserInfo {
        @GET("user/info")
        fun get_userInfo(): Call<UserInfoResponse>

        @Multipart
        @PATCH("user/update/img")
        fun update_img(
            @Part file: MultipartBody.Part
        ): Call<UserImgUpdatedResponse>
    }

    interface UserNickChange{
        @PATCH("user/update/nickname")
        @Headers("content-type: application/json", "accept: application/json")
        fun change_nickname(
                @Body request: UserNickRequest
        ): Call<UserInfoResponse>
    }

    // 알림창
    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)
        if (type.equals("Ok")){
            dialog.setTitle("닉네임 수정")
            dialog.setMessage("완료되었습니다")
        }

        val dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d("닉네임", "다이얼로그")
                }
            }
        }
        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }
}