package com.example.mc.navigationbar

import android.content.Context.INPUT_METHOD_SERVICE
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mc.R
import com.example.mc.database.EventDatabase
import com.example.mc.databinding.FragmentReportBinding
import com.example.mc.model.Marker
import com.example.mc.model.Markers
import com.example.mc.repository.EventRepository
import com.example.mc.viewmodel.EventViewModel
import com.example.mc.viewmodel.ReportFragmentViewModel
import com.example.mc.viewmodelfactory.EventViewModelFactory
import com.example.mc.viewmodelfactory.ReportFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReportFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var eventViewModel: EventViewModel

    private var lat: Double = 0.0
    private var long: Double = 0.0

    lateinit var descriptionEt: EditText
    lateinit var reportBtn: Button
    lateinit var addressTv: TextView

    private var familyName: String = ""
    private var givenName: String = ""
    private var email: String = ""


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

        auth = Firebase.auth

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentReportBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_report, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao
        val viewModelFactory = ReportFragmentViewModelFactory(dataSource, application)

        descriptionEt = binding.root.findViewById(R.id.description)
        reportBtn = binding.root.findViewById(R.id.reportBtn)
        addressTv = binding.root.findViewById(R.id.addressTv)

        Firebase.database.getReference("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    familyName = dataSnapshot.child("firstName").value.toString()
                    givenName = dataSnapshot.child("givenName").value.toString()
                    email = dataSnapshot.child("email").value.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

        reportBtn.isAllCaps = false

        fusedLocationClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    lat = location.latitude
                    long = location.longitude
                    Log.d("TAG", lat.toString() + "long" + long.toString())
                    getAddressInfoLocation(lat, long)
                }
            }

        val reportViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[ReportFragmentViewModel::class.java]

        reportBtn.setOnClickListener {
            val current = LocalDateTime.now()
            val dateIso = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

            Toast.makeText(context, "Event reported successfully", Toast.LENGTH_SHORT).show()

            addMarker(email, lat, long, "$familyName $givenName", dateIso)

            // hide keyboard after submit
            val imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            descriptionEt.text.clear()
        }

        binding.lifecycleOwner = this

        binding.reportFragmentViewModel = reportViewModel
        return binding.root
    }

    private fun addMarker(email: String, lat: Double, long: Double, s: String, current: String) {
        val eventRepository = EventRepository()
        val eventViewModelFactory = EventViewModelFactory(eventRepository)
        eventViewModel = ViewModelProvider(this, eventViewModelFactory)[EventViewModel::class.java]

        val markers = Markers(listOf(Marker(email, lat, long, s, current)))

        Log.d("Markers_list", markers.toString())
        eventViewModel.addMarker(markers)
        eventViewModel.addMarkerResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                Log.d("Successful", "Success $response")
            } else {
                Log.d("Not successful", response.toString())
            }
        }
    }

    private fun getAddressInfoLocation(lat: Double, long: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(lat, long, 1) as List<Address>
        val address = addresses[0]
        val addressText = address.getAddressLine(0)
        addressTv.text = addressText
    }
}