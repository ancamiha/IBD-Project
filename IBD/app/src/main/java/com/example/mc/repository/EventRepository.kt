package com.example.mc.repository

import com.example.mc.model.Markers
import com.example.mc.model.Members
import com.example.mc.retrofit.RetrofitHelper
import retrofit2.Response

class EventRepository {
    suspend fun addMarker(markers: Markers): Response<Void> {
        return RetrofitHelper.api.addMarker(markers)
    }

    suspend fun getMarkers(): Response<Members> {
        return RetrofitHelper.api.getMarkers()
    }
}