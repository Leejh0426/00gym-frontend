package com.example.a00gym.DataClass

import com.google.gson.annotations.SerializedName

data class GymReservation(
    @SerializedName("gymStatusId") val gymStatusId: Int,
    @SerializedName("reservationNumber") val reservationNumber: Int
)
