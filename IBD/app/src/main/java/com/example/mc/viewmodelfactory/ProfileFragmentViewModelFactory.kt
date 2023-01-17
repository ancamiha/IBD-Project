package com.example.mc.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mc.database.EventDatabaseDao
import com.example.mc.viewmodel.ProfileFragmentViewModel

class ProfileFragmentViewModelFactory (
    private val dataSource: EventDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java)) {
            return ProfileFragmentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}