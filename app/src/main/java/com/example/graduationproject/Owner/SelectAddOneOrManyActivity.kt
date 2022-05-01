package com.example.graduationproject.Owner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivitySelectAddOneOrManyBinding

class SelectAddOneOrManyActivity : AppCompatActivity () {
    private lateinit var binding: ActivitySelectAddOneOrManyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAddOneOrManyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        binding.SelectAddMany.setOnClickListener {

        }

        binding.SelectAddOne.setOnClickListener {
            startActivity(Intent(this, MartListActivity::class.java))
        }
    }

}