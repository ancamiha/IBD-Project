package com.example.mc

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener {
            onItemSelectedNavBar(it)
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sign_out -> {
                    Firebase.auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.startActivity(intent)
                    true
                }
                else -> true
            }
        }
    }

    private fun onItemSelectedNavBar(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.navigation_home -> {
                findNavController(R.id.nav_fragment).navigate(R.id.navigation_home)
                true
            }
            R.id.navigation_report -> {
                findNavController(R.id.nav_fragment).navigate(R.id.navigation_report)
                true
            }
            R.id.navigation_profile -> {
                findNavController(R.id.nav_fragment).navigate(R.id.navigation_profile)
                true
            }
        }
        return true
    }
}