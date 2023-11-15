package com.example.a00gym

import com.google.gson.annotations.SerializedName

data class Gym(
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: String,
    @SerializedName("gym_name") val gymName: String,
    @SerializedName("total_number") val totalNumber: Int
)