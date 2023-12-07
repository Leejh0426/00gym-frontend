package com.example.a00gym.`interface`

import com.example.a00gym.dataclass.GymInquiryResponse
import com.example.a00gym.dataclass.GymReservation
import com.example.a00gym.dataclass.GymReservationResponse
import com.example.a00gym.dataclass.GymResponse
import com.example.a00gym.dataclass.GymStatusResponse
import com.example.a00gym.dataclass.ReservationCResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GymInterface {
    @GET("/gym") // 체육관 list get요청
    fun getGyms(@Query("location") location: String): Call<GymResponse>

    @GET("/gymstatus") // 체육관 현재 정보 get요청
    fun getGymDetails(@Query("id") id: Int, @Query("time") time: String): Call<GymStatusResponse>

    @POST("/reservation") // 체육관 id와 사용자가 입력한 숫자로 Post요청
    fun postGymStatus(@Body gymReservation: GymReservation): Call<GymReservationResponse>

    @GET("/reservation") // 예약조회를 위한 get요청
    fun getGymInquiry(@Query("reservationDate") reservationDate: String, @Query("dateTime") dateTime: String,): Call<GymInquiryResponse>

    @DELETE("/reservation/{reservationId}") // 예약취소를 위한 delete요청
    fun deleteReservation(@Path("reservationId") reservationId: Int): Call<ReservationCResponse>
}