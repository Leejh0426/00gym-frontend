package com.example.a00gym.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.example.a00gym.R

class CustomDialog(context: Context, private val onCancel: () -> Unit, private val onYes: () -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        val btnYes = findViewById<Button>(R.id.btnYes)
        val btnCancel = findViewById<Button>(R.id.btnNo)

        btnYes.setOnClickListener { // Yes버튼 눌렀을 때 동작
            onYes.invoke()
            dismiss()
        }

        btnCancel.setOnClickListener { // No버튼 눌렀을 때 동작
            onCancel.invoke()
            dismiss()
        }
    }
}