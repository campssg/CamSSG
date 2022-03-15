package com.example.graduationproject.Owner

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R

class EditMartActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_martinfo)

        val spinner1 = findViewById<Spinner>(R.id.mart_time_start)
        val spinner2 = findViewById<Spinner>(R.id.mart_time_end)

        spinner1.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)
        spinner2.adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item)
    }
}