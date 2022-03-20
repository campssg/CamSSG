package com.example.graduationproject.Owner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityOwnermainBinding
import com.example.graduationproject.databinding.ActivitySetMartBinding

class SetMartActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetMartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetMartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "마트 등록"

        val spinner1 = binding.martTimeStart
        val spinner2 = binding.martTimeEnd

        spinner1.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)
        spinner2.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)

        //등록하기 누르면 메인화면으로
        binding.martInfoSubmit.setOnClickListener {
            Toast.makeText(this, "마트가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            var outintent = Intent(applicationContext, OwnerMainActivity::class.java)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }

    }
}