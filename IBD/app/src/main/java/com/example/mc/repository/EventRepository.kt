package com.example.mc.repository

import com.example.mc.model.Markers
import com.example.mc.retrofit.RetrofitHelper
import retrofit2.Response

class EventRepository {
    suspend fun addMarker(markers: Markers): Response<String> {
        return RetrofitHelper.api.addMarker(markers)
    }
}