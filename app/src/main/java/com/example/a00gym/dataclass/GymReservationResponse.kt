package com.example.a00gym.dataclass

import com.google.gson.annotations.SerializedName

data class GymReservationResponse(
    @SerializedName("result") val result: String
) : GymBase()
