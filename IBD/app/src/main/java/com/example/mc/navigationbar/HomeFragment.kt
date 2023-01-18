package com.example.mc.navigationbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.mc.R
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

        return view
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
                    val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                    mapFragment.getMapAsync { googleMap ->
                        val latLng = LatLng(lat, long)
                        val zoom = 15f
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

                        val markers = listOf(
                            LatLng(lat+0.005, long+0.005),
                            LatLng(lat-0.005, long-0.005),
                            LatLng(lat+0.01, long-0.01)
                        )

                        for (marker in markers) {
                            googleMap.addMarker(MarkerOptions().position(marker).title("Type").snippet("Description"))
                        }
                    }
                }
            }


    }

}