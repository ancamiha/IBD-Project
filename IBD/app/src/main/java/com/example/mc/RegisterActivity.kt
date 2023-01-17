package com.example.mc

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val backBtn: Button = findViewById(R.id.backButton)
        val registerBtn: Button = findViewById(R.id.registerBtn)
        val nameEt: EditText = findViewById(R.id.name)
        val givenNameEt: EditText = findViewById(R.id.givenName)
        val emailEt: EditText = findViewById(R.id.email)
        val passwordEt: EditText = findViewById(R.id.password)
        val password2Et: EditText = findViewById(R.id.password2)

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        registerBtn.isAllCaps = false
        registerBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val givenName = givenNameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val password2 = password2Et.text.toString()

            if (name.isEmpty()) {
                nameEt.error = "Name is required!"
                nameEt.requestFocus()
                return@setOnClickListener
            }
            if (givenName.isEmpty()) {
                givenNameEt.error = "Username is required!"
                givenNameEt.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                emailEt.error = "Email is required!"
                emailEt.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEt.error = "Please provide valid email!"
                emailEt.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEt.error = "Password is required!"
                passwordEt.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                passwordEt.error = "Min password length is 6 characters!"
                passwordEt.requestFocus()
                return@setOnClickListener
            }
            if (password2.isEmpty()) {
                password2Et.error = "Password is required!"
                password2Et.requestFocus()
                return@setOnClickListener
            }
            if (password2 != password) {
                password2Et.error = "The password is not the same!"
                password2Et.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = User(name, givenName, email)

                        Firebase.database.getReference("users")
                            .child(auth.currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "User successfully created. Please login.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    auth.signOut()
                                    val intent = Intent(baseContext, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Failed to register! Try again!",
                                        Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                    } else {
                        Toast.makeText(this, "Failed to register! Try again!", Toast.LENGTH_LONG)
                            .show();
                    }
                }
        }
    }

}