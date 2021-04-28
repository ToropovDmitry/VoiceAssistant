package com.example.voiceassistent.NumberInformation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.voiceassistent.Interfeces.InfoNumberApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer

class InfoNumberToString {
    fun getInfoNumber(
        number: String?,
        callback: Consumer<String?>
    ) {
        val api: InfoNumberApi? = InfoNumberService().getApi()
        val call: Call<InfoNumber?>? = api?.getCurrentNumber(number)
        call?.enqueue(object : Callback<InfoNumber?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<InfoNumber?>,
                response: Response<InfoNumber?>
            ) {
                val result = response.body()
                if (result != null) {
                    callback.accept(result.current)
                } else {
                    callback.accept("Не могу получить факт о числе")
                }
            }
            override fun onFailure(
                call: Call<InfoNumber?>,
                t: Throwable
            ) {
                Log.w("INFONUMBER", t.message!!)
            }
        })
    }
}