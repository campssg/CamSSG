package com.example.graduationproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AddItemOneActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_one)

        val spinner = findViewById<Spinner>(R.id.item_type)
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item)
    }
}