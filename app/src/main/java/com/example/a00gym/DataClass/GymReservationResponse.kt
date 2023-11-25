package com.example.a00gym.DataClass

import com.google.gson.annotations.SerializedName

data class GymReservationResponse(
    @SerializedName("result") val result: String
) : GymBase()
