package com.example.graduationproject.UserSigning

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.example.graduationproject.RegisterActivity2
import com.example.graduationproject.databinding.ActivityLoginBinding
import com.example.graduationproject.databinding.ActivityMainBinding
import com.example.graduationproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    val TAG:String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private lateinit var binding: ActivityRegisterBinding
    val EmailData = arrayOf("naver.com","nate.com","daum.net","gmail.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()



        //이메일 선택 스피너

        val emailAdapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,EmailData)
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.Emailspinner.adapter = emailAdapter



        //정보 api 전송
        binding.RegisterBtn.setOnClickListener {
            Log.d(TAG,"회원가입 버튼 클릭")
            val name = binding.editTextTextPersonName.text.toString()
            val email = binding.editTextTextEmailAddress2.text.toString()
            val phoneNumber = binding.editTextNumber1.text.toString()
            val phoneNumber2 = binding.editTextNumber2.text.toString()
            val phoneNumber3 = binding.editTextNumber3.text.toString()
            val Password = binding.editTextTextPassword.text.toString()
            val PasswordCheck = binding.editTextTextPassword2.text.toString()


            if(name.isEmpty()||email.isEmpty()||phoneNumber.isEmpty()||phoneNumber2.isEmpty()||phoneNumber3.isEmpty()||Password.isEmpty()||PasswordCheck.isEmpty()){
               isExistBlank = true
        }
            else{
                if(Password==PasswordCheck){
                    isPWSame = true
                }

            }

            if(!isExistBlank && isPWSame){
                Toast.makeText(this, "회원가입 성공",Toast.LENGTH_SHORT).show()

                val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("name",name)
                editor.putString("email",email)
                editor.putString("phoneNumber",phoneNumber)
                editor.putString("phoneNumber2",phoneNumber2)
                editor.putString("phoneNumber3",phoneNumber3)
                editor.putString("Password",Password)
                editor.putString("PasswordCheck",PasswordCheck)
                editor.apply()

                //사업자번호 입력 페이지로 이동
                val intent = Intent(this,RegisterActivity2::class.java)
                startActivity(intent)

            }
            else{
                if(isExistBlank){
                    dialog("Blank")
                }
                else if(!isPWSame){
                    dialog("not same")
                }
            }

        }

        //이메일 스피너 만들기

        //버튼 눌렀을 때 입력되지 않은 곳 있으면 입력해달라고 하기(하나하나 찝지 말고 그냥 어딘가 비었다고만 알려주기)
        //비밀번호 제한 걸기(8-20자 이내의 숫자와 대소문자 혼합)



    }


    fun dialog(type:String){
        val dialog = AlertDialog.Builder(this)
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listener = object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG,"다이얼로그")
                }

            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}