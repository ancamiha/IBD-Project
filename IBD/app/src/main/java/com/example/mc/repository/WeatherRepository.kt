package com.example.mc.repository

import com.example.mc.model.OpenWeatherResponse
import com.example.mc.retrofit.RetrofitHelper
import retrofit2.Response

class WeatherRepository {
    suspend fun getWeatherData(lat:Double, long:Double, appId:String): Response<OpenWeatherResponse> {
        return RetrofitHelper.api.getWeatherData(lat, long, appId)
    }
}