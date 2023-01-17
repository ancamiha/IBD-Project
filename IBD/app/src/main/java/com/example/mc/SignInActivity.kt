package com.example.mc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        val backBtn: Button = findViewById(R.id.backButton)
        val emailEt: EditText = findViewById(R.id.email_sign_in)
        val passwordEt: EditText = findViewById(R.id.password_sign_in)
        val singInBtn: Button = findViewById(R.id.singInBtn)

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        singInBtn.isAllCaps = false
        singInBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty()) {
                emailEt.error = "Email is required!"
                emailEt.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEt.error = "Password is required!"
                passwordEt.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(baseContext, ContainerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "The email or password is incorrect!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}