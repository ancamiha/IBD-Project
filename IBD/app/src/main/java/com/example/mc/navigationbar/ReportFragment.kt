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
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mc.R
import com.example.mc.database.EventDatabase
import com.example.mc.databinding.FragmentReportBinding
import com.example.mc.viewmodel.ReportFragmentViewModel
import com.example.mc.viewmodelfactory.ReportFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.util.*

class ReportFragment : Fragment() {

    private var lat: Double = 0.0
    private var long: Double = 0.0

    lateinit var descriptionEt: EditText
    lateinit var spinnerEvents: Spinner
    lateinit var reportBtn: Button
    lateinit var addressTv: TextView

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

        val binding: FragmentReportBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_report, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao
        val viewModelFactory = ReportFragmentViewModelFactory(dataSource, application)

        descriptionEt = binding.root.findViewById(R.id.description)
        spinnerEvents = binding.root.findViewById(R.id.spinner_events)
        reportBtn = binding.root.findViewById(R.id.reportBtn)
        addressTv = binding.root.findViewById(R.id.addressTv)


        reportBtn.isAllCaps = false
        ArrayAdapter.createFromResource(
            context!!, R.array.events, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerEvents.adapter = adapter
        }

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
                this, viewModelFactory)[ReportFragmentViewModel::class.java]

        reportBtn.setOnClickListener{
            reportViewModel.insertEvent(
                addressTv.text.toString(),
                LocalDate.now().toString(),
                spinnerEvents.selectedItem.toString(),
                descriptionEt.text.toString()
            )
            Toast.makeText(context, "Event reported successfully", Toast.LENGTH_SHORT).show()
            // hide keyboard after submit
            val imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            descriptionEt.text.clear()
            spinnerEvents.setSelection(0)
        }

        binding.lifecycleOwner = this

        binding.reportFragmentViewModel = reportViewModel
        return binding.root
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