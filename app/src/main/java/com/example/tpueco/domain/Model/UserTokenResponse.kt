package com.example.tpueco.domain.Model

import com.google.gson.annotations.SerializedName

data class UserTokenResponse(
    @field:  SerializedName("access_token")
    var access_token: String? = null,
    @field:  SerializedName("refresh_token")
    var refresh_token: String? = null,
    @field:  SerializedName("expires_in")
    var expires_in: String? = null
)
