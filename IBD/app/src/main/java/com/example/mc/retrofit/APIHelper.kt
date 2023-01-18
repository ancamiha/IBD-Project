package com.example.mc.retrofit

import com.example.mc.model.Marker
import com.example.mc.model.OpenWeatherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIHelper {
    @POST("/add_markers")
    suspend fun addMarker(@Body marker: List<Marker>): Response<String>
}