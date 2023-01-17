package com.example.mc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        val user : FirebaseUser? = Firebase.auth.currentUser
        user?.let {
            val intent = Intent(baseContext, ContainerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerBtn: Button = findViewById(R.id.registerBtn)
        val singInBtn: Button = findViewById(R.id.singInBtn)

        registerBtn.isAllCaps = false
        singInBtn.isAllCaps = false

        registerBtn.setOnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        singInBtn.setOnClickListener {
            val intent = Intent(baseContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        requestPermissions()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
}