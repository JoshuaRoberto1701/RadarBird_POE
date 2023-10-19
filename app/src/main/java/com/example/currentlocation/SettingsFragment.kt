package com.example.currentlocation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.SharedPreferences
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.settings, container, false)

        sharedPref = activity?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE) ?: return view

        val unitRadioGroup: RadioGroup = view.findViewById(R.id.unitRadioGroup)
        val distanceSeekBar: SeekBar = view.findViewById(R.id.distanceSeekBar)
        val distanceTextView: TextView = view.findViewById(R.id.distanceTextView)
        val btnLogOut: Button = view.findViewById(R.id.btnLogOut)

        // Load saved max distance and unit from shared preferences
        val savedMaxDistance = sharedPref.getInt("MaxDistance", 0)
        val savedUnit = sharedPref.getString("Unit", "km") // Default to "km" if not set

        distanceSeekBar.progress = savedMaxDistance
        distanceTextView.text = "Max Distance: $savedMaxDistance $savedUnit"

        if (savedUnit == "km") {
            unitRadioGroup.check(R.id.metricRadioButton)
        } else {
            unitRadioGroup.check(R.id.imperialRadioButton)
        }

        // Update based on radio button selection
        unitRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val unit = if (checkedId == R.id.metricRadioButton) "km" else "miles"
            val currentProgress = distanceSeekBar.progress
            distanceTextView.text = "Max Distance: $currentProgress $unit"
            sharedPref.edit().putString("Unit", unit).apply()
        }

        // Update based on seek bar movement
        distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val unit = if (view.findViewById<RadioButton>(R.id.metricRadioButton).isChecked) "km" else "miles"
                distanceTextView.text = "Max Distance: $progress $unit"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.edit().putInt("MaxDistance", seekBar?.progress ?: 0).apply()
            }
        })
        // Add click listener to btnLogOut
        btnLogOut.setOnClickListener {
            // Log out the user and navigate to the Home screen
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }
}
