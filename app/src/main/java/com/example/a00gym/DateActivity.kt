package com.example.a00gym

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class DateActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        datePicker = findViewById(R.id.datePicker)
        btnNext = findViewById(R.id.inquiry)

        btnNext.setOnClickListener {
            // DatePicker에서 선택한 날짜를 가져오기
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth

            // 선택한 날짜를 Calendar 객체에 설정
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)

            // SimpleDateFormat을 사용하여 선택한 날짜를 문자열로 변환
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormat.format(selectedDate.time)

            // 다음 화면으로 데이터를 전달하는 Intent 생성
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("SELECTED_DATE", dateString)

            // 다음 화면으로 이동
            startActivity(intent)
        }

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }

}
