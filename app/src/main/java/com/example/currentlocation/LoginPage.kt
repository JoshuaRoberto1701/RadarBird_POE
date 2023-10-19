package com.example.currentlocation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginPage : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignIn: Button

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnSignIn = findViewById(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            // Authenticate the user using Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Authentication successful, show a welcome message as a Toast
                        val welcomeMessage = "Welcome back to Radar Bird!, $email!"
                        Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show()
                        // Navigate to HostActivity or perform any other actions
                        val intent = Intent(this, HostActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Authentication failed, display an error message
                        Toast.makeText(
                            this,
                            "Login failed. Email or password is incorrect.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
