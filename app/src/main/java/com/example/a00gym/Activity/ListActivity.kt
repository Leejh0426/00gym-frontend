package com.example.a00gym.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a00gym.DataClass.GymResponse
import com.example.a00gym.Interface.GymInterface
import com.example.a00gym.R
import com.example.a00gym.RetrofitClient.GymRetrofitClient
import com.example.a00gym.databinding.ActivityListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ListActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var mBinding : ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.location.text

        val testList = resources.getStringArray(R.array.gym_List)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, testList)
        mBinding.spinner.adapter = adapter

        mBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Log.d("FlightList", "선택")
                Log.d("선택", "선택못함")
                mBinding.location.text = parent.getItemAtPosition(position).toString()
                getGymData(parent.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        // Intent에서 전달된 날짜 정보를 받아서 표시
        val selectedDate = intent.getStringExtra("SELECTED_DATE")

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val formattedDate = try {
            val date = inputFormat.parse(selectedDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            selectedDate // 변환이 실패할 경우 그대로 사용
        }
        tvSelectedDate = findViewById(R.id.reserve_date)
        tvSelectedDate.text = "날짜: $formattedDate"

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }
    }
    private fun updateUI(gymResponse: GymResponse) {
        val listView = findViewById<ListView>(R.id.listView)

        // Gym 이름을 추출하여 리스트에 추가합니다.
        val gyms = gymResponse.result
        val gymNames = gyms.map { it.gymName }

        // ArrayAdapter를 사용하여 ListView에 데이터를 설정합니다.
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, gymNames)
        listView.adapter = adapter

        // ListView의 아이템을 클릭했을 때의 동작을 설정할 수 있습니다.
        listView.setOnItemClickListener { _, _, position, _ ->
            // 원하는 동작을 수행합니다. 예를 들어, 해당 위치의 Gym 데이터를 가져와서 다음 액티비티로 전달할 수 있습니다.
            val selectedGym = gyms[position]
            val selectedDate = intent.getStringExtra("SELECTED_DATE")
            val intent = Intent(this, TimelistActivity::class.java)
            intent.putExtra("SELECTED_GYM_ID", selectedGym.id)
            intent.putExtra("SELECTED_GYM_NAME", selectedGym.gymName)
            intent.putExtra("SELECTED_DATE", selectedDate)
            intent.putExtra("SELECTED_LOCATION", selectedGym.location)

            val preferencesName = "MyPreferences"

            // 쉐어드 프리퍼런스에 데이터 저장
            val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            // 처음 선택한 지역 저장
            editor.putString("selectedLocation", selectedGym.location)
            // 체육관 이름 저장
            editor.putString("selectedGymName", selectedGym.gymName)
            // 변경 사항을 반영
            editor.apply()

            startActivity(intent)
        }
    }
    private fun getGymData(location: String) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.getGyms(location).enqueue(object : Callback<GymResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GymResponse>, response: Response<GymResponse>) {
                if (response.isSuccessful) {
                    Log.d("Gymlist", "체육관 목록 요청 성공")
                    Log.d("Gymlist", "받은 데이터: ${response.body()}")

                    updateUI(response.body() as GymResponse)
                }
            }

            override fun onFailure(call: Call<GymResponse>, t: Throwable) {
                Log.d("GymList", "체육관 목록 요청 실패")
            }
        })
    }
}



