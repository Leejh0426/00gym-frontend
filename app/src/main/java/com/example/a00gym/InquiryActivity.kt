package com.example.a00gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class InquiryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry)

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }
}