package com.example.a00gym

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.a00gym.DataClass.GymInquiry
import com.example.a00gym.Interface.GymInterface
import com.example.a00gym.RetrofitClient.GymRetrofitClient
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class InquiryActivity : AppCompatActivity() {
    private lateinit var tvselectedGymName: TextView
    private lateinit var tvbookStatus: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry)

        // 쉐어드 프리퍼런스에서 데이터 불러오기
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        // 저장된 지역과 체육관 이름 불러오기 (기본값은 빈 문자열)
        val savedLocation = sharedPreferences.getString("selectedLocation", "")
        val savedGymName = sharedPreferences.getString("selectedGymName", "")
        // 쉐어드 프리퍼런스에서 데이터 불러오기
        val sharedPreferences1 = getSharedPreferences("MyPreferences1", Context.MODE_PRIVATE)
        // 저장된 총원 정보 불러오기
        val savedtotalNumber = sharedPreferences1.getInt("TOTAL_NUMBER", 0)

        val reservationNumber = intent.getIntExtra("RESERVATION_NUMBER", -1)
        val reservationDate = intent.getStringExtra("SELECTED_DATE")
        val dateTime = intent.getStringExtra("SELECTED_DATETIME")

        tvselectedGymName = findViewById(R.id.gym_info)
        tvselectedGymName.text = "지역/풋살장명: $savedLocation/$savedGymName"

        tvbookStatus = findViewById(R.id.BookStatus)
        tvbookStatus.text = "풋살장 예약현황: $reservationNumber / $savedtotalNumber "



        getGymInquiry(reservationDate.toString(), dateTime.toString())

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
    private fun updateUI(gyms: List<GymInquiry>) {
        val reservationNumber = findViewById<TextView>(R.id.reservationNumber)
        val date = findViewById<TextView>(R.id.date)
        val dateTime = findViewById<TextView>(R.id.dateTime)
        if (gyms.isNotEmpty()) {
            val firstGym = gyms[0]
            reservationNumber.text = "내 예약인원: ${firstGym.reservationNumber}"
        } else {
            reservationNumber.text = "예약된 내역이 없습니다."
        }
        if (gyms.isNotEmpty()) {
            val firstGym = gyms[0]
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val formattedDate = try {
                val date = inputFormat.parse(firstGym.reservationDate)
                outputFormat.format(date)
            } catch (e: Exception) {
                firstGym.reservationDate 
            }
            date.text = "예약날짜: $formattedDate"
        } else {
            date.text = "예약된 내역이 없습니다."
        }
        if (gyms.isNotEmpty()) {
            val firstGym = gyms[0]
            dateTime.text = "예약시간: ${firstGym.dateTime}"
        } else {
            dateTime.text = "예약된 내역이 없습니다."
        }
    }

    private fun getGymInquiry(reservationDate: String, dateTime: String) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.getGymInquiry(reservationDate, dateTime).enqueue(object : Callback<List<GymInquiry>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<GymInquiry>>, response: Response<List<GymInquiry>>) {
                if (response.isSuccessful) {
                    Log.d("InquiryActivity", "예약조회 성공")
                    Log.d("InquiryActivity", "받은 데이터: ${response.body()}")

                    updateUI(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<GymInquiry>>, t: Throwable) {
                Log.d("InquiryActivity", "예약조회 실패: ${t.message}")
            }
        })
    }
}