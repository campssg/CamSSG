package com.example.graduationproject.UserSigning

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.example.graduationproject.Owner.OwnerMainActivity
import com.example.graduationproject.RegisterResponse
import com.example.graduationproject.User.UserMainActivity
import com.example.graduationproject.databinding.Activity1registeruserBinding
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class RegisterActivity_User : AppCompatActivity() {

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private lateinit var binding: Activity1registeruserBinding
    val EmailData = arrayOf("naver.com", "nate.com", "daum.net", "gmail.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1registeruserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        //val gson = GsonBuilder().setLenient().create()

        val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()

        //http://13.124.13.202:8080/api/v1/login
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.13.202:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()


        val service = retrofit.create(SignService::class.java)


        //이메일 선택 스피너

        val emailAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, EmailData)
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.Emailspinner.adapter = emailAdapter


        val emailAddress = EmailData[binding.Emailspinner.selectedItemPosition]

        //스피너 값을 텍스트로 받기



        //정보 api 전송
        binding.RegisterBtn.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")
            val name = binding.editTextTextPersonName.text.toString()
            val email = binding.editTextTextEmailAddress2.text.toString()+"@"+emailAddress
            val phoneNumber = binding.editTextNumber1.text.toString()
            val phoneNumber2 = binding.editTextNumber2.text.toString()
            val phoneNumber3 = binding.editTextNumber3.text.toString()
            val phoneNumberTotal = phoneNumber + phoneNumber2 + phoneNumber3
            val Password = binding.editTextTextPassword.text.toString()
            val PasswordCheck = binding.editTextTextPassword2.text.toString()

            service.register(phoneNumberTotal, email, name, Password)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        val result = response.body()
                        Log.e("회원가입 성공","${result}")
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.e("회원가입 실패",t.message.toString())
                    }

                })





            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || phoneNumber2.isEmpty() || phoneNumber3.isEmpty() || Password.isEmpty() || PasswordCheck.isEmpty()) {
                isExistBlank = true
            } else {
                if (Password == PasswordCheck) {
                    isPWSame = true
                }

            }

            if (!isExistBlank && isPWSame) {
                Toast.makeText(this, "액티비티상 회원가입 성공", Toast.LENGTH_SHORT).show()

                val sharedPreference = getSharedPreferences("file name", MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("name", name)
                editor.putString("email", email)
                editor.putString("phoneNumber", phoneNumber)
                editor.putString("phoneNumber2", phoneNumber2)
                editor.putString("phoneNumber3", phoneNumber3)
                editor.putString("Password", Password)
                editor.putString("PasswordCheck", PasswordCheck)
                editor.apply()


                val intent = Intent(this, LoginActivity_User::class.java)
                startActivity(intent)

            } else {
                if (isExistBlank) {
                    dialog("Blank")
                } else if (!isPWSame) {
                    dialog("not same")
                }
            }

        }


        //버튼 눌렀을 때 입력되지 않은 곳 있으면 입력해달라고 하기
        //비밀번호 제한 걸기


    }

    interface SignService {
        //phoneNumberTotal,email,name,Password
        @FormUrlEncoded
        @POST("user/register/guest")
        fun register(
            @Field("phoneNumber") phoneNumberTotal: String,
            @Field("userEmail") email: String,
            @Field("userName") name: String,
            @Field("userPassword") Password: String
        ): Call<RegisterResponse>

    }




    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)
        if (type.equals("blank")) {
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        } else if (type.equals("not same")) {
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }

            }
        }
        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }

    // 뒤로가기 이벤트
    override fun onBackPressed() {
        AlertDialog.Builder(this@RegisterActivity_User)
            .setTitle("가입 취소")
            .setMessage("회원 가입을 그만두시겠습니까?")
            .setPositiveButton("예", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    finish()
                }
            })
            .setNegativeButton("아니오", object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
            .create()
            .show()
    }
}