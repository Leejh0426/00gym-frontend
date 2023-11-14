package com.example.a00gym

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class DateData (
    @SerializedName("id") val id: Int,
    @SerializedName("time") val time: String,
)