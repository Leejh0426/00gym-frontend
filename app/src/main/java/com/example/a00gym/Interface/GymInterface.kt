package com.example.a00gym.Interface

import com.example.a00gym.DataClass.GymInquiryResponse
import com.example.a00gym.DataClass.GymReservation
import com.example.a00gym.DataClass.GymReservationResponse
import com.example.a00gym.DataClass.GymResponse
import com.example.a00gym.DataClass.GymStatusResponse
import com.example.a00gym.DataClass.ReservationCancleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GymInterface {
    @GET("/gym")
    fun getGyms(@Query("location") location: String): Call<GymResponse>

    @GET("/gymstatus")
    fun getGymDetails(@Query("id") id: Int, @Query("time") time: String): Call<GymStatusResponse>

    @POST("/reservation")
    fun postGymStatus(@Body gymReservation: GymReservation): Call<GymReservationResponse>

    @GET("/reservation")
    fun getGymInquiry(@Query("reservationDate") reservationDate: String, @Query("dateTime") dateTime: String,): Call<GymInquiryResponse>

    @DELETE("/reservation/{reservationId}")
    fun deleteReservation(@Path("reservationId") reservationId: Int): Call<ReservationCancleResponse>
}