package com.example.a00gym.Interface

import com.example.a00gym.DataClass.Gym
import com.example.a00gym.DataClass.GymReservation
import com.example.a00gym.DataClass.GymStatus
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GymInterface {
    @GET("/gym")
    fun getGyms(@Query("location") location: String): Call<List<Gym>>

    @GET("/gymstatus")
    fun getGymDetails(@Query("id") id: Int, @Query("time") time: String): Call<List<GymStatus>>

    @POST("/reservation")
    fun postGymStatus(@Body gymReservation: GymReservation): Call<List<String>>
}