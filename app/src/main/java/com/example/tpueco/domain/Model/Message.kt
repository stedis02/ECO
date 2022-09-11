package com.example.tpueco.domain.Model


data class Message(
    var from: String = "",
    var header: String = "",
    var Text: String?,
    val date: MessageDate
)
