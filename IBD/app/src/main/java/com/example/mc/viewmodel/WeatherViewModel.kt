package com.example.mc.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mc.model.OpenWeatherResponse
import com.example.mc.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    val getWeatherResponse: MutableLiveData<Response<OpenWeatherResponse>> = MutableLiveData()

    fun getWeatherData(lat:Double, long:Double, appId:String) {
        viewModelScope.launch {
            val response = weatherRepository.getWeatherData(lat, long, appId)
            getWeatherResponse.value = response
        }
    }
}