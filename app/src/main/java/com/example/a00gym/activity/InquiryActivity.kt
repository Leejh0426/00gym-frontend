package com.example.a00gym.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.a00gym.dialog.CustomDialog
import com.example.a00gym.dataclass.GymInquiryResponse
import com.example.a00gym.dataclass.ReservationCResponse
import com.example.a00gym.`interface`.GymInterface
import com.example.a00gym.R
import com.example.a00gym.dataclass.ErrorResponse
import com.example.a00gym.retrofitClient.GymRetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class InquiryActivity : AppCompatActivity(){
    private lateinit var tvselectedGymName: TextView
    private lateinit var tvbookStatus: TextView
    private lateinit var gymInquiryResponse: GymInquiryResponse
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
        val savedTotalNumber = sharedPreferences1.getInt("TOTAL_NUMBER", 0)


        val savedReservationNumber = sharedPreferences.getString("reservationNumber", "")
        val reservationDate = intent.getStringExtra("SELECTED_DATE")
        val dateTime = intent.getStringExtra("SELECTED_DATETIME")
        Log.d("InquiryActivity", "$reservationDate")
        Log.d("InquiryActivity", "$dateTime")

        tvselectedGymName = findViewById(R.id.gym_info)
        tvselectedGymName.text = "지역/풋살장명: $savedLocation/$savedGymName"

        tvbookStatus = findViewById(R.id.BookStatus)
        tvbookStatus.text = "풋살장 예약현황: $savedReservationNumber / $savedTotalNumber "



        getGymInquiry(reservationDate.toString(), dateTime.toString())

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val clBtn = findViewById<View>(R.id.cancle_btn)
        clBtn.setOnClickListener {
            showCancelDialog(gymInquiryResponse)
        }
    }
    private fun updateUI(gymInquiryResponse: GymInquiryResponse) {
        val gyms = gymInquiryResponse.result
        val reservationNumber = findViewById<TextView>(R.id.reservationNumber)
        val date = findViewById<TextView>(R.id.date)
        val dateTime = findViewById<TextView>(R.id.dateTime)
        if (gyms.isNotEmpty()) {
            val firstGym = gyms.last()
            reservationNumber.text = "내 예약인원: ${firstGym.reservationNumber}"
        }
        if (gyms.isNotEmpty()) {
            val firstGym = gyms.last()
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val formattedDate = try {
                val date = inputFormat.parse(firstGym.reservationDate)
                outputFormat.format(date)
            } catch (e: Exception) {
                firstGym.reservationDate 
            }
            date.text = "예약날짜: $formattedDate"
        }
        if (gyms.isNotEmpty()) {
            val firstGym = gyms.last()
            dateTime.text = "예약시간: ${firstGym.dateTime}"
        }
    }

    // 예약한 날짜와 예약 시간을 통해 예약조회를 위한 get 요청
    private fun getGymInquiry(reservationDate: String, dateTime: String) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.getGymInquiry(reservationDate, dateTime).enqueue(object : Callback<GymInquiryResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GymInquiryResponse>, response: Response<GymInquiryResponse>) {
                if (response.isSuccessful) {
                    Log.d("InquiryActivity", "예약조회 성공")
                    Log.d("InquiryActivity", "받은 데이터: ${response.body()}")

                    gymInquiryResponse = response.body() as GymInquiryResponse
                    updateUI(gymInquiryResponse)
                }
            }

            override fun onFailure(call: Call<GymInquiryResponse>, t: Throwable) {
                Log.d("InquiryActivity", "예약조회 실패: ${t.message}")
            }
        })
    }
    // 예약취소 다이얼로그를 띄웠을 때 할 동작 함수 정의
    private fun showCancelDialog(gymInquiryResponse: GymInquiryResponse) {
        val gyms1 = gymInquiryResponse.result
        val gyms2 = gyms1.last()
        // Yes 버튼을 누르면 reservationId로 delete요청
        val dialog = CustomDialog(this,
            onCancel = {},
            onYes = {
                deleteReservation(gyms2.reservationId)
                Log.d("InquiryActivity", "삭제요청: ${gyms2.reservationId}")
            }
        )
        dialog.show()
    }
    // 예약한 reservationId를 가지고 delete 요청
    private fun deleteReservation(reservationId: Int) {
        Log.d("InquiryActivity", "삭제 요청 시작: $reservationId")
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.deleteReservation(reservationId).enqueue(object : Callback<ReservationCResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ReservationCResponse>, response: Response<ReservationCResponse>) {
                if (response.isSuccessful) {
                    Log.d("InquiryActivity", "삭제 요청 성공")
                    Log.d("InquiryActivity", "받은 데이터: ${response.body()}")

                } else {
                    // 실패 시 수행할 작업
                    Log.d("InquiryActivity", "삭제 요청 실패")
                    Log.d("InquiryActivity", "삭제 요청 응답 코드: ${response.code()}")
                    try {
                        // 실패 시 서버 응답의 상세 내용을 로그에 출력
                        val errorBody = response.errorBody()?.string()
                        Log.d("InquiryActivity", "Error Body: $errorBody")

                        val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        showToast(message = errorMessage.message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ReservationCResponse>, t: Throwable) {
                Log.d("InquiryActivity", "삭제 요청 실패: ${t.message}")
            }
        })
        // 삭제 함수 호출
        clearSharedPreferences()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    private fun showToast(message: String) {
        Toast.makeText(this@InquiryActivity, message, Toast.LENGTH_SHORT).show()
    }

    // 쉐어드 프리퍼런스 데이터 삭제
    private fun clearSharedPreferences() {
        Log.d("InquiryActivity", "clearSharedPreferences() 시작")
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val sharedPreferences1 = getSharedPreferences("MyPreferences1", Context.MODE_PRIVATE)
        sharedPreferences1.edit().clear().apply()
        Log.d("InquiryActivity", "clearSharedPreferences() 완료")
    }
}