package com.example.mc.retrofit

import com.example.mc.model.Markers
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIHelper {
    @POST("/add_markers")
    suspend fun addMarker(@Body markers: Markers): Response<String>
}