package com.example.voiceassistent.NumberInformation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InfoNumber : Serializable {
    @SerializedName("text")
    @Expose
    var current: String? = null
}