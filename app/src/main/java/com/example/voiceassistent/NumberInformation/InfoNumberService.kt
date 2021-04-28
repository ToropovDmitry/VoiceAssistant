package com.example.voiceassistent.NumberInformation

import com.example.voiceassistent.Interfeces.InfoNumberApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoNumberService {
    fun getApi(): InfoNumberApi? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://numbersapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(InfoNumberApi::class.java)
    }
}