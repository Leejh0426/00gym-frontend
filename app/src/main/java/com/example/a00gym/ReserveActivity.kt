package com.example.a00gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ReserveActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedHour: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        tvSelectedDate = findViewById(R.id.reserve_date3)
        tvSelectedDate.text = "날짜: $selectedDate"

        tvSelectedHour = findViewById(R.id.reserve_time)
        btnNext = findViewById(R.id.reserve)

        // Intent에서 전달된 시간 정보를 받아서 표시
        val selectedHour = intent.getStringExtra("SELECTED_HOUR")
        tvSelectedHour.text = "Selected Hour: $selectedHour"

        btnNext.setOnClickListener {
            // 다음 화면으로 데이터를 전달하는 Intent 생성
            val intent = Intent(this, InquiryActivity::class.java)
            intent.putExtra("SELECTED_HOUR", selectedHour)

            // 다음 화면으로 이동
            startActivity(intent)
        }
    }
}
