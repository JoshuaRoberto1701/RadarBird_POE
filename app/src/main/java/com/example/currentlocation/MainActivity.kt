package com.example.currentlocation

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.*
import java.io.IOException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isMyLocationButtonEnabled = true

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                fetchBirdHotspots(it.latitude, it.longitude)
            }
        }
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Radius of the Earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distance in km
    }

    private fun fetchBirdHotspots(userLat: Double, userLng: Double) {
        val apiKey = "biekti07bd7m"  // Replace this with your actual API key
        val client = OkHttpClient()

        // Fetch the saved max distance and unit from shared preferences
        val maxDistance = sharedPref.getInt("MaxDistance", 0) // Use 0 as default if not set
        val unit = sharedPref.getString("Unit", "km") // Default to "km" if not set

        // Convert max distance to kilometers if the saved unit is miles
        val distanceInKm = if (unit == "miles") maxDistance * 1.60934 else maxDistance.toDouble()

        // Append the max distance to the API request URL
        val request = Request.Builder()
            .url("https://api.ebird.org/v2/ref/hotspot/geo?lat=$userLat&lng=$userLng&fmt=json")
            .header("X-eBirdApiToken", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("EBIRD_API", "Failed to fetch data: ", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonData = response.body?.string()
                jsonData?.let {
                    val jsonArray: JsonArray = JsonParser.parseString(it).asJsonArray

                    for (jsonElement in jsonArray) {
                        val jsonObject = jsonElement.asJsonObject
                        val hotspotLat = jsonObject.get("lat").asDouble
                        val hotspotLng = jsonObject.get("lng").asDouble
                        val locName = jsonObject.get("locName").asString

                        val distanceToHotspot = haversine(userLat, userLng, hotspotLat, hotspotLng)
                        if (distanceToHotspot <= distanceInKm) {
                            runOnUiThread {
                                mMap.addMarker(MarkerOptions().position(LatLng(hotspotLat, hotspotLng)).title(locName))
                            }
                        }
                    }
                }
            }
        })
    }
}
