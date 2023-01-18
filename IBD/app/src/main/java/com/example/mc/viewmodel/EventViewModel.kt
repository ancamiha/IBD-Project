package com.example.mc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mc.model.Marker
import com.example.mc.model.OpenWeatherResponse
import com.example.mc.repository.EventRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    val addMarkerResponse: MutableLiveData<Response<String>> = MutableLiveData()

    fun addMarker(marker: List<Marker>) {
        viewModelScope.launch {
            val response = eventRepository.addMarker(marker)
            addMarkerResponse.value = response
        }
    }
}