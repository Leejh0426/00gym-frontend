package com.example.a00gym.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a00gym.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val reserve = findViewById<View>(R.id.reserve)
        reserve.setOnClickListener {
            val intent = Intent(this, DateActivity::class.java)
            startActivity(intent)
        }
        val reserveInquiry = findViewById<View>(R.id.reserve_inquiry)
        reserveInquiry.setOnClickListener {

            val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

            // 저장된 지역과 체육관 이름 불러오기 (기본값은 빈 문자열)
            val savedLocation = sharedPreferences.getString("selectedLocation", "")
            val savedGymName = sharedPreferences.getString("selectedGymName", "")

            if (savedLocation.isNullOrBlank() || savedGymName.isNullOrBlank()) {
                // 저장된 데이터가 없을 경우 Toast를 표시하고 함수 종료
                Toast.makeText(this, "예약현황이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, InquiryActivity::class.java)
            startActivity(intent)
        }
    }
}