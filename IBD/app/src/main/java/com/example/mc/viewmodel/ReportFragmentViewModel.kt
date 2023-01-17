package com.example.mc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mc.database.EventDatabaseDao
import com.example.mc.model.Event
import kotlinx.coroutines.launch

class ReportFragmentViewModel (
    val database: EventDatabaseDao,
    application: Application
) : AndroidViewModel(application){

    fun insertEvent(loc: String, data: String, tag: String, desc: String) {
        val event: Event = Event(location = loc, data = data, tag = tag, description = desc)
        viewModelScope.launch {
            database.insert(event)
        }
    }
}