package com.example.mc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mc.model.Markers
import com.example.mc.model.Members
import com.example.mc.repository.EventRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    val addMarkerResponse: MutableLiveData<Response<Void>> = MutableLiveData()
    val getMarkersResponse: MutableLiveData<Response<Members>> = MutableLiveData()

    fun addMarker(markers: Markers) {
        viewModelScope.launch {
            val response = eventRepository.addMarker(markers)
            addMarkerResponse.value = response
        }
    }

    fun getMarkers() {
        viewModelScope.launch {
            val response = eventRepository.getMarkers()
            getMarkersResponse.value = response
        }
    }
}