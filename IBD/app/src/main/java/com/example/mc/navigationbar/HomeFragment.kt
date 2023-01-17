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
    lateinit var iconUrl: String

    lateinit var weather: TextView
    lateinit var weatherDesc: TextView
    lateinit var tempMax: TextView
    lateinit var tempMin: TextView
    lateinit var tempNow: TextView
    lateinit var tempFeel: TextView
    lateinit var pressure: TextView
    lateinit var humidity: TextView
    lateinit var wind: TextView

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

        weather = view.findViewById(R.id.weather)
        weatherDesc = view.findViewById(R.id.weather_desc)
        tempMax = view.findViewById(R.id.temp_max)
        tempMin = view.findViewById(R.id.temp_min)
        tempNow = view.findViewById(R.id.temp_now)
        tempFeel = view.findViewById(R.id.temp_feel)
        pressure = view.findViewById(R.id.pressure)
        humidity = view.findViewById(R.id.humidity)
        wind = view.findViewById(R.id.wind)

        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    lat = location.latitude
                    long = location.longitude
                    Log.d("TAG", lat.toString() + "long" + long.toString())
                    getWeatherData(lat, long)
                }
            }

        return view
    }

    private fun kelvinToCelsius(kelvin: Double): String {
        return String.format("%.1f", (kelvin - 272.15)) + "\u00B0C"
    }

    private fun getWeatherData(lat: Double, long: Double) {
        val weatherRepository = WeatherRepository()
        val weatherViewModelFactory = WeatherViewModelFactory(weatherRepository)
        weatherViewModel = ViewModelProvider(this, weatherViewModelFactory)[WeatherViewModel::class.java]

        weatherViewModel.getWeatherData(lat, long, "bacbd4b660a4c7f5df567558bc91b0a1")
        weatherViewModel.getWeatherResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val weatherResponse: OpenWeatherResponse = response.body()!!
                weather.append(weatherResponse.weather!![0].main)
                weatherDesc.append(weatherResponse.weather!![0].description)

                tempMax.append(kelvinToCelsius(weatherResponse.main!!.temp_max))
                tempMin.append(kelvinToCelsius(weatherResponse.main!!.temp_min))
                tempNow.append(kelvinToCelsius(weatherResponse.main!!.temp))
                tempFeel.append(kelvinToCelsius(weatherResponse.main!!.feels_like))

                pressure.append(weatherResponse.main!!.pressure.toString() + " hPa")
                humidity.append(weatherResponse.main!!.humidity.toString() + "%")
                wind.append(weatherResponse.wind!!.speed.toString() + " m/s")
            } else {
                Log.d("Response", response.errorBody().toString())
            }
        }
    }
}