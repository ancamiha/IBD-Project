package com.example.mc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mc.database.EventDatabaseDao
import com.example.mc.model.Event

class ProfileFragmentViewModel (
    val database: EventDatabaseDao,
    application: Application
) : AndroidViewModel(application){

    private val _events = MediatorLiveData<List<Event>>()

    init {
        _events.addSource(database.getAllEvents()) {
            _events.value = it
        }
    }

    val events: LiveData<List<Event>>
        get() = _events

}