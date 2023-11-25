package com.example.a00gym

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.a00gym.DataClass.GymInquiry
import com.example.a00gym.databinding.CustomDialogBinding

class CustomDialog(context: Context, private val onCancel: () -> Unit, private val onYes: () -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        val btnYes = findViewById<Button>(R.id.btnYes)
        val btnCancel = findViewById<Button>(R.id.btnNo)

        btnYes.setOnClickListener {
            onYes.invoke()
            dismiss()
        }

        btnCancel.setOnClickListener {
            onCancel.invoke()
            dismiss()
        }
    }
}