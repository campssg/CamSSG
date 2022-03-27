package com.example.graduationproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivityAddItemOneBinding

class AddItemOneActivity :AppCompatActivity() {
    private lateinit var binding: ActivityAddItemOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemOneBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val spinner = findViewById<Spinner>(R.id.item_type)
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item)
    }
}