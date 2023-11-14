package com.example.a00gym

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a00gym.databinding.ActivityListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {
    private lateinit var tvSelectedDate: TextView
    private lateinit var mBinding : ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.gymName.text

        val testList = resources.getStringArray(R.array.gym_List)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, testList)
        mBinding.spinner.adapter = adapter

        mBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Log.d("FlightList", "선택")
                mBinding.gymName.text = parent.getItemAtPosition(position).toString()
                getGymData(parent.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        tvSelectedDate = findViewById(R.id.reserve_date)

        // Intent에서 전달된 날짜 정보를 받아서 표시
        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        tvSelectedDate.text = "날짜: $selectedDate"

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val inquiry2 = findViewById<View>(R.id.inquiry2)
        inquiry2.setOnClickListener {
            val intent = Intent(this, TimelistActivity::class.java)
            intent.putExtra("SELECTED_DATE", selectedDate)
            startActivity(intent)
        }
    }
    private fun getGymData(location: String) {
        val gymInterface = GymRetrofitClient.fRetrofit.create(GymInterface::class.java)
        gymInterface.getGyms(location).enqueue(object : Callback<List<Gym>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<Gym>>, response: Response<List<Gym>>) {
                if (response.isSuccessful) {
                    Log.d("Gymlist", "체육관 목록 요청 성공")
                }
            }

            override fun onFailure(call: Call<List<Gym>>, t: Throwable) {
                Log.d("GymList", "체육관 목록 요청 실패")
            }
        })
    }
}



