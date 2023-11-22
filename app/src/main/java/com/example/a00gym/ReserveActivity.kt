package com.example.a00gym

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.a00gym.DataClass.GymReservation
import com.example.a00gym.Interface.GymInterface
import com.example.a00gym.RetrofitClient.GymRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ReserveActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvselectedDateTime: TextView
    private lateinit var tvselectedGymName: TextView
    private lateinit var btnNext: Button
    private lateinit var selectedlocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        selectedlocation = intent.getStringExtra("SELECTED_LOCATION").toString()
        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        tvSelectedDate = findViewById(R.id.reserve_date3)
        tvSelectedDate.text = "날짜: $selectedDate"

        val selectedDateTime = intent.getStringExtra("SELECTED_DATETIME")
        tvselectedDateTime = findViewById(R.id.reserve_time)
        tvselectedDateTime.text = "시간: $selectedDateTime"

        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val savedGymName = sharedPreferences.getString("selectedGymName", "")
        tvselectedGymName = findViewById(R.id.gym_name)
        tvselectedGymName.text = "풋살장명: $savedGymName"

        val selectedGymStatusId = intent.getIntExtra("SELECTED_GYMSTATUS_ID", -1)
        Log.d("ReserveActivity", "Selected Gym ID: $selectedGymStatusId")

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedDate = try {
            val date = inputFormat.parse(selectedDateTime)
            outputFormat.format(date)
        } catch (e: Exception) {
            selectedDate // 변환이 실패할 경우 그대로 사용
        }
        tvSelectedDate = findViewById(R.id.reserve_time)
        tvSelectedDate.text = "날짜: $formattedDate"
        btnNext = findViewById(R.id.reserveationNumber)
        btnNext.setOnClickListener {
            val userInputNumber = findViewById<EditText>(R.id.reserve_people).text.toString()// EditText 등에서 사용자가 입력한 숫자를 받아옴
            Log.d("ReserveActivity", "넣은 숫자: ${userInputNumber.toInt()}")
            val gymReservation = GymReservation(gymStatusId = selectedGymStatusId, reservationNumber = userInputNumber.toInt())
            postGymStatusToServer(gymReservation)
            // 다음 화면으로 데이터를 전달하는 Intent 생성
            val intent = Intent(this, InquiryActivity::class.java)

            intent.putExtra("RESERVATION_NUMBER", userInputNumber.toInt())
            intent.putExtra("SELECTED_DATE", selectedDate)
            intent.putExtra("SELECTED_DATETIME", selectedDateTime)
            startActivity(intent)
        }
        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
}

private fun postGymStatusToServer(gymReservation: GymReservation) {
    val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        val call = gymInterface.postGymStatus(gymReservation)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    // 성공 시 수행할 작업
                    Log.d("ReserveActivity", "post 요청 성공")
                } else {
                    // 실패 시 수행할 작업
                    Log.d("ReserveActivity", "post 요청 실패")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                t.printStackTrace()
                // 실패 시 수행할 작업
                Log.d("ReserveActivity", "post 요청 실패2: ${t.message}")
            }
        })
    }

