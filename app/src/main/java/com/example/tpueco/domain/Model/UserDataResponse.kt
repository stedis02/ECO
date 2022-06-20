package com.example.tpueco.domain.Model

import com.google.gson.annotations.SerializedName


data class UserDataResponse(
    @field:  SerializedName("user_id")
    val user_id: String,
    @field:  SerializedName("lichnost_id")
    val lichnost_id: String,
    @field:  SerializedName("lichnost")
    val lichnost: LichnostResponse,
    @field:  SerializedName("email")
    val email: String,
    @field:  SerializedName("message")
    val message: String,
    @field:  SerializedName("code")
    val code: String
)
