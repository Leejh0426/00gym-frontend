package com.example.a00gym

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GymInterface {
    @GET("/gym")
    fun getGyms(@Query("location") location: String): Call<List<Gym>>

}