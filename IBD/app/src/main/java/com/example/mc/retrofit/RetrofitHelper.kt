package com.example.mc.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "https://7051-2a02-2f0f-c409-f400-40f7-f70-75b9-25b5.ngrok.io"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: APIHelper by lazy {
        retrofit.create(APIHelper::class.java)
    }
}