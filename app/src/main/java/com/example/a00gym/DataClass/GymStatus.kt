package com.example.a00gym.DataClass

import com.google.gson.annotations.SerializedName

data class GymStatus(
    @SerializedName("id") val id: Int,
    @SerializedName("remainder") val remainder: String,
    @SerializedName("dateTime") val dateTime: String,
    @SerializedName("totalNumber") val totalNumber: Int,
    @SerializedName("condValue") val condValue: Boolean
)
