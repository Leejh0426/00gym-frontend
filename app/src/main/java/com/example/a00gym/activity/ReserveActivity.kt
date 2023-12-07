package com.example.a00gym.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.a00gym.dataclass.ErrorResponse
import com.example.a00gym.dataclass.GymReservation
import com.example.a00gym.dataclass.GymReservationResponse
import com.example.a00gym.`interface`.GymInterface
import com.example.a00gym.R
import com.example.a00gym.retrofitClient.GymRetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ReserveActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvselectedDateTime: TextView
    private lateinit var tvselectedGymName: TextView
    private lateinit var tvselectedLocation: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val selectedLocation = sharedPreferences.getString("selectedLocation", "")
        tvselectedLocation = findViewById(R.id.location1)
        tvselectedLocation.text = "지역: $selectedLocation"

        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = try { // 날짜 정보만 화면에 표출하도록 변환해서 저장
            val date = inputFormat.parse(selectedDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            selectedDate // 변환이 실패할 경우 그대로 사용
        }
        tvSelectedDate = findViewById(R.id.reserve_date)
        tvSelectedDate.text = "예약한 날: $formattedDate"

        val selectedDateTime = intent.getStringExtra("SELECTED_DATETIME")
        tvselectedDateTime = findViewById(R.id.reserve_time)
        tvselectedDateTime.text = "예약날짜/시간: $selectedDateTime"

        val savedGymName = sharedPreferences.getString("selectedGymName", "")
        tvselectedGymName = findViewById(R.id.gym_name)
        tvselectedGymName.text = "풋살장명: $savedGymName"

        val selectedGymStatusId = intent.getIntExtra("SELECTED_GYMSTATUS_ID", -1)
        Log.d("ReserveActivity", "Selected Gym ID: $selectedGymStatusId")

        btnNext = findViewById(R.id.reservationNumber)
        btnNext.setOnClickListener { // btnNext 버튼을 눌러 예약하고 post요청
            val userInputNumber = findViewById<EditText>(R.id.reserve_people).text.toString()// EditText 등에서 사용자가 입력한 숫자를 받아옴
            Log.d("ReserveActivity", "넣은 숫자: ${userInputNumber.toInt()}")
            val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("reservationNumber", userInputNumber)
            editor.apply()
            val gymReservation = GymReservation(gymStatusId = selectedGymStatusId, reservationNumber = userInputNumber.toInt())
            postGymStatusToServer(gymReservation) {
                Log.d("post", "post요청 제발 성공 제발")
                // 다음 화면으로 데이터를 전달하는 Intent 생성
                val intent = Intent(this, InquiryActivity::class.java)
                intent.putExtra("SELECTED_DATE", selectedDate)
                intent.putExtra("SELECTED_DATETIME", selectedDateTime)
                startActivity(intent)
            }
        }
        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this@ReserveActivity, message, Toast.LENGTH_SHORT).show()
    }
    // reservationId를 이용해 post 요청
    private fun postGymStatusToServer(gymReservation: GymReservation, onComplete: () -> Unit) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        val call = gymInterface.postGymStatus(gymReservation)

        call.enqueue(object : Callback<GymReservationResponse> {
            override fun onResponse(call: Call<GymReservationResponse>, response: Response<GymReservationResponse>) {
                if (response.isSuccessful) {
                    // 성공 시 수행할 작업
                    Log.d("ReserveActivity", "post 요청 성공")
                    Log.d("ReserveActivity", "post 요청 응답 코드: ${response.code()}")
                    Log.d("ReserveActivity", "post 요청 응답 데이터: ${response.body()}")
                    onComplete()
                } else {
                    // 실패 시 수행할 작업
                    Log.d("ReserveActivity", "post 요청 실패")
                    Log.d("ReserveActivity", "post 요청 응답 코드: ${response.code()}")
                    try {
                        // 실패 시 서버 응답의 상세 내용을 로그에 출력
                        val errorBody = response.errorBody()?.string()
                        Log.d("ReserveActivity", "Error Body: $errorBody")

                        val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        showToast(message = errorMessage.message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<GymReservationResponse>, t: Throwable) {
                t.printStackTrace()
                Log.d("ReserveActivity", "post 요청 실패2: ${t.message}")
            }
        })

    }
}

