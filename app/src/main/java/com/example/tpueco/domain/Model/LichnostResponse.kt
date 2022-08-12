package com.example.tpueco.domain.Model

import com.google.gson.annotations.SerializedName

data class LichnostResponse(
    @field:  SerializedName("familiya")
    val familiya: String,
    @field:  SerializedName("imya")
    val imya: String
)
