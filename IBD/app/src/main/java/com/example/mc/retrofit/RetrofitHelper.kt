package com.example.mc.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "https://57c0-2a02-2f0f-c409-f400-9cf6-6f5a-194f-6503.ngrok.io"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: APIHelper by lazy {
        retrofit.create(APIHelper::class.java)
    }
}