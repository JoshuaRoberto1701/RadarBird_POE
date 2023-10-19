package com.example.currentlocation

import YourInitialFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        // Set the initial fragment if needed
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, YourInitialFragment())
                .commit()
        }
    }
}
