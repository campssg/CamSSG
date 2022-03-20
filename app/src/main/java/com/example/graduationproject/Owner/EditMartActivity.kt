package com.example.graduationproject.Owner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMartinfoBinding
import com.example.graduationproject.databinding.ActivitySetMartBinding

//마트 정보 수정

class EditMartActivity :AppCompatActivity(){
    private lateinit var binding: ActivityMartinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMartinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner1 = binding.martTimeStart
        val spinner2 = binding.martTimeEnd

        spinner1.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)
        spinner2.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)

        //수정하기 누를 시 메인화면으로 이동
        binding.martInfoSubmit.setOnClickListener {
            Toast.makeText(this, "마트 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            var outintent = Intent(applicationContext, OwnerMainActivity::class.java)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
    }
}