package com.example.a00gym.dataclass

import com.google.gson.annotations.SerializedName

data class GymInquiry(
@SerializedName("reservationId") val reservationId: Int,
@SerializedName("reservationDate") val reservationDate: String,
@SerializedName("reservationNumber") val reservationNumber: Int,
@SerializedName("dateTime") val dateTime: String
)
