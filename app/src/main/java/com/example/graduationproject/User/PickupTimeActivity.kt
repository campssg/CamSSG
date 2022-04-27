package com.example.graduationproject.User

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.Activity1pickupTimeBinding
import com.example.graduationproject.databinding.ActivityLoginBinding
import java.util.*

class PickupTimeActivity : AppCompatActivity() {
    private lateinit var binding : Activity1pickupTimeBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity1pickupTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이틀 숨기기
        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        // 선택한 마트 정보 가져오기
        val martname = binding.martName
        val martnum = binding.martNum
        val martadr = binding.martAddress



        // 날짜 설정
        val settingDate = binding.calendarSet

        val spinner1 = binding.timePick


        spinner1.adapter = ArrayAdapter.createFromResource(this, R.array.pu_time_array, android.R.layout.simple_spinner_item)

        val text = spinner1.selectedItem.toString()

        val sharedPreferences = getSharedPreferences("putime", MODE_PRIVATE)
        sharedPreferences.edit().putString("putime", text).apply()

        settingDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.setDate.setText("${year} - ${month+1} - ${dayOfMonth}")

                }
            }, year, month, date)

            dlg.show()
        }


    }
}