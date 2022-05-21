package com.example.testovoegit.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(
    @field:  SerializedName("login")
    val login: String? = null,
    @field:  SerializedName("id")
    val id: Int? = null,
    @field:  SerializedName("avatar_url")
    val avatar_url: String? = null
)
