package com.example.voiceassistent.Interfeces

import com.example.voiceassistent.NumberInformation.InfoNumber
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoNumberApi  {
    @GET("/{number}/trivia?json")
    fun getCurrentNumber(@Path("number") number: String?): Call<InfoNumber?>?
}