package com.example.mc.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mc.repository.WeatherRepository
import com.example.mc.viewmodel.WeatherViewModel

class WeatherViewModelFactory(private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(weatherRepository) as T
    }
}