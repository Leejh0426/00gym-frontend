package com.example.a00gym.dataclass

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)
