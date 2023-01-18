package com.example.mc.repository

import com.example.mc.model.Marker
import com.example.mc.model.OpenWeatherResponse
import com.example.mc.retrofit.RetrofitHelper
import retrofit2.Response

class EventRepository {
    suspend fun addMarker(marker: List<Marker>): Response<String> {
        return RetrofitHelper.api.addMarker(marker)
    }
}