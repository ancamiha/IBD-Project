package com.example.mc.navigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mc.R
import com.example.mc.adapter.EventListAdapter
import com.example.mc.database.EventDatabase
import com.example.mc.databinding.FragmentProfileBinding
import com.example.mc.viewmodel.ProfileFragmentViewModel
import com.example.mc.viewmodelfactory.ProfileFragmentViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var familyNameTv: TextView
    private lateinit var givenNameTv: TextView
    private lateinit var emailTv: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noEventsTv: TextView

    private lateinit var viewModel: ProfileFragmentViewModel
    private lateinit var eventListAdapter: EventListAdapter

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

        eventListAdapter = EventListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao
        val viewModelFactory = ProfileFragmentViewModelFactory(dataSource, application)

        familyNameTv = binding.root.findViewById(R.id.familyName)
        givenNameTv = binding.root.findViewById(R.id.givenName)
        emailTv = binding.root.findViewById(R.id.email)
        noEventsTv = binding.root.findViewById(R.id.emptyList)
        recyclerView = binding.root.findViewById(R.id.event_list)

        Firebase.database.getReference("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    familyNameTv.text = dataSnapshot.child("firstName").value.toString()
                    givenNameTv.text = dataSnapshot.child("givenName").value.toString()
                    emailTv.text = dataSnapshot.child("email").value.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

        recyclerView.adapter = eventListAdapter

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileFragmentViewModel::class.java)
        viewModel.events.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                noEventsTv.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                eventListAdapter.events = it
            } else {
                noEventsTv.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

}