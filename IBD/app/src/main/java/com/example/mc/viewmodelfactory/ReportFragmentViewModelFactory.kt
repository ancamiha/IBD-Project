package com.example.mc.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mc.database.EventDatabaseDao
import com.example.mc.viewmodel.ReportFragmentViewModel

class ReportFragmentViewModelFactory (
    private val dataSource: EventDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportFragmentViewModel::class.java)) {
            return ReportFragmentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}