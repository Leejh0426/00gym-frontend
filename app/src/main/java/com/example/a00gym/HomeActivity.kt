package com.example.a00gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val reserve = findViewById<View>(R.id.reserve)
        reserve.setOnClickListener {
            val intent = Intent(this, DateActivity::class.java)
            startActivity(intent)
        }
    }
}