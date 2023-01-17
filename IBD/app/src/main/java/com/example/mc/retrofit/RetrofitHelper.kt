package com.example.mc.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: APIHelper by lazy {
        retrofit.create(APIHelper::class.java)
    }
}