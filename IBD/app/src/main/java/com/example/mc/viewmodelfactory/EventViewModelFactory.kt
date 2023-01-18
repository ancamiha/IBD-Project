package com.example.mc.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mc.repository.EventRepository
import com.example.mc.viewmodel.EventViewModel

class EventViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventViewModel(eventRepository) as T
    }
}