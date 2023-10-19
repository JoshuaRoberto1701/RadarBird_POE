package com.example.currentlocation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home) // This assumes your layout file is named home.xml

        // Initialize buttons
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Set click listener for btnLogin
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)  // Assuming LoginPage is the name of the activity you want to open
            startActivity(intent)
        }

        // Set click listener for btnRegister
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)  // Assuming RegisterPage is the name of the activity you want to open
            startActivity(intent)
        }
    }
}
