package com.example.tpueco.domain.Model

import android.os.Parcelable

data class Message(var from: String = "", var header: String = "", var Text: String?, val date: MessageDate)
