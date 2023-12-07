package com.example.a00gym.dataclass

import com.google.gson.annotations.SerializedName

data class GymReservation(
    @SerializedName("gymStatusId") val gymStatusId: Int,
    @SerializedName("reservationNumber") val reservationNumber: Int
)
