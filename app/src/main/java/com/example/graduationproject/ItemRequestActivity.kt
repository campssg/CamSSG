package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import com.example.graduationproject.databinding.ActivityItemRequestBinding


//요청상품 확인 페이지


class ItemRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()



        val spinner = binding.RequestType
        spinner.adapter = ArrayAdapter.createFromResource(this,R.array.item_request,android.R.layout.simple_spinner_item)


    }
}

