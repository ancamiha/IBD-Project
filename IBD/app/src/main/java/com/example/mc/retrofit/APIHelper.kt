package com.example.mc.retrofit

import com.example.mc.model.OpenWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIHelper {
    @GET("weather")
    suspend fun getWeatherData(@Query("lat") lat:Double, @Query("lon") long: Double, @Query("appid") appId: String): Response<OpenWeatherResponse>
}