package com.example.a00gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Spinner

class TimelistActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var spinnerHours: Spinner
    private lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timelist)

        // Intent에서 전달된 날짜 정보를 받아서 표시
        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        tvSelectedDate = findViewById(R.id.reserve_date2)
        tvSelectedDate.text = "날짜: $selectedDate"

        spinnerHours = findViewById(R.id.spinnerHours)
        btnNext = findViewById(R.id.choice)

        val hours = arrayOf("08~10", "10~12", "12~14", "14~16", "16~18", "18~20", "20~22", "22~24")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hours)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHours.adapter = adapter

        btnNext.setOnClickListener {

            val selectedHour = spinnerHours.selectedItem.toString()
            val intent2 = Intent(this, ReserveActivity::class.java)
            intent2.putExtra("SELECTED_HOUR", selectedHour)
            intent2.putExtra("SELECTED_DATE", selectedDate)
            // Spinner에서 선택한 시간 가져오기
            // 다음 화면으로 이동
            startActivity(intent2)
        }

    }
}