package com.example.a00gym.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a00gym.Adapter.GymStatusAdapter
import com.example.a00gym.DataClass.GymStatus
import com.example.a00gym.DataClass.GymStatusResponse
import com.example.a00gym.Interface.GymInterface
import com.example.a00gym.R
import com.example.a00gym.RetrofitClient.GymRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class TimelistActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedGymName: TextView
    private lateinit var btnNext2: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var gymStatusAdapter: GymStatusAdapter
    private lateinit var selectedlocation: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timelist)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        selectedlocation = intent.getStringExtra("SELECTED_LOCATION").toString()
        val selectedGymID = intent.getIntExtra("SELECTED_GYM_ID", -1)
        Log.d("TimelistActivity", "Selected Gym ID: $selectedGymID")
        // Intent에서 전달된 날짜 정보를 받아서 표시
        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        Log.d("TimelistActivity", "SelectedDate: $selectedDate")

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = try {
            val date = inputFormat.parse(selectedDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            selectedDate // 변환이 실패할 경우 그대로 사용
        }
        tvSelectedDate = findViewById(R.id.reserve_date2)
        tvSelectedDate.text = "날짜: $formattedDate"


        val selectedGymName = intent.getStringExtra("SELECTED_GYM_NAME")

        tvSelectedGymName = findViewById(R.id.top_title)
        tvSelectedGymName.text = "$selectedGymName"


        btnNext2 = findViewById(R.id.inquiry3)
        btnNext2.setOnClickListener {
            Log.d("get", "get요청 제발 성공해줘 제발")
            getGymStatus(selectedGymID, selectedDate.toString())
        }

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
    private fun updateUI(gymStatusResponse: GymStatusResponse) {
        // TextView에 연결된 체육관 정보를 설정
        val gyms = gymStatusResponse.result
        gymStatusAdapter = GymStatusAdapter(gyms)
        recyclerView.adapter = gymStatusAdapter

        gymStatusAdapter.setOnItemClickListener(object : GymStatusAdapter.OnItemClickListener {
            override fun onItemClick(position: GymStatus) {
                try {
                    // 선택한 GymStatus 객체를 가져옴
                    val selectedGymStatus = position
                    val selectedDate = intent.getStringExtra("SELECTED_DATE")
                    val selectedGymName = intent.getStringExtra("SELECTED_GYM_NAME")

                    // 다음 액티비티로 전달할 정보를 Intent에 추가
                    val intent = Intent(this@TimelistActivity, ReserveActivity::class.java)
                    intent.putExtra("SELECTED_DATETIME", selectedGymStatus.dateTime)
                    intent.putExtra("SELECTED_GYMSTATUS_ID", selectedGymStatus.id)
                    intent.putExtra("SELECTED_DATE", selectedDate)
                    intent.putExtra("SELECTED_GYM_NAME", selectedGymName)
                    intent.putExtra("TOTAL_NUMBER", selectedGymStatus.totalNumber)

                    val preferencesName1 = "MyPreferences1"

                    // 쉐어드 프리퍼런스에 데이터 저장
                    val sharedPreferences1 = getSharedPreferences(preferencesName1, Context.MODE_PRIVATE)
                    val editor = sharedPreferences1.edit()
                    //get요청으로 불러온 총원 저장
                    editor.putInt("TOTAL_NUMBER", selectedGymStatus.totalNumber)
                    // 변경 사항을 반영
                    editor.apply()

                    // 다음 액티비티 시작
                    startActivity(intent)
                } catch (e: Exception) {
                    // 예외가 발생하면 로그에 출력
                    e.printStackTrace()
                    Log.e("YourActivityName", "클릭 이벤트 도중 예외 발생: ${e.message}")
                }
            }

        })
    }

    // 한 체육관의 예약 시간과, 수용 인원을 표출하기 위해 get 요청
    private fun getGymStatus(id: Int, selectedDate: String) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.getGymDetails(id, selectedDate).enqueue(object : Callback<GymStatusResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GymStatusResponse>, response: Response<GymStatusResponse>) {
                if (response.isSuccessful) {
                    Log.d("Gymlist", "체육관 목록 추가 요청 성공")
                    Log.d("Gymlist", "받은 데이터: ${response.body()}")

                    updateUI(response.body() as GymStatusResponse)
                }
            }

            override fun onFailure(call: Call<GymStatusResponse>, t: Throwable) {
                Log.d("GymList", "체육관 목록 추가 요청 실패: ${t.message}")
            }
        })
    }
}
