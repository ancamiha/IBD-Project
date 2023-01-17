package com.example.mc.navigationbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mc.R
import com.example.mc.model.OpenWeatherResponse
import com.example.mc.repository.WeatherRepository
import com.example.mc.viewmodel.WeatherViewModel
import com.example.mc.viewmodelfactory.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {
    lateinit var weatherViewModel: WeatherViewModel

    private var lat: Double = 0.0
    private var long: Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.moveTaskToBack(true)
                }
            })
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)



        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    lat = location.latitude
                    long = location.longitude
                    Log.d("TAG", lat.toString() + "long" + long.toString())
                }
            }

        return view
    }

    private fun kelvinToCelsius(kelvin: Double): String {
        return String.format("%.1f", (kelvin - 272.15)) + "\u00B0C"
    }
}