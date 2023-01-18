package com.example.mc.navigationbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mc.R
import com.example.mc.model.Members
import com.example.mc.repository.EventRepository
import com.example.mc.viewmodel.EventViewModel
import com.example.mc.viewmodelfactory.EventViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment() {

    private var lat: Double = 0.0
    private var long: Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var eventViewModel: EventViewModel
    private lateinit var markers: Members

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    lat = location.latitude
                    long = location.longitude
                    Log.d("TAG", lat.toString() + "long" + long.toString())
                    getMarkersAndConfigMap(lat, long)
                }
            }

    }

    private fun getMarkersAndConfigMap(lat: Double, long: Double) {
        val eventRepository = EventRepository()
        val eventViewModelFactory = EventViewModelFactory(eventRepository)
        eventViewModel = ViewModelProvider(this, eventViewModelFactory)[EventViewModel::class.java]

        eventViewModel.getMarkers()
        eventViewModel.getMarkersResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                markers = response.body()!!

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                mapFragment.getMapAsync { googleMap ->
                    val latLng = LatLng(lat, long)
                    val zoom = 10f
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

                    for (marker in markers.members) {
                        googleMap.addMarker(
                            MarkerOptions().position(LatLng(marker.lat, marker.lng)).title(marker.address).snippet(marker.name)
                        )
                    }
                }

                Log.d("Successful", response.body().toString())
            } else {
                Log.d("Not successful", response.errorBody().toString())
            }
        }
    }
}