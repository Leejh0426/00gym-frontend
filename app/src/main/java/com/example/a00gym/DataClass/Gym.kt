package com.example.a00gym.DataClass

import com.google.gson.annotations.SerializedName

data class Gym(
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: String,
    @SerializedName("gymName") val gymName: String,
    @SerializedName("totalNumber") val totalNumber: Int
)