package com.example.a00gym

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GymRetrofitClient {
    val fRetrofit = initRetrofit()
    private const val URL = "https://00gym.shop"
    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}