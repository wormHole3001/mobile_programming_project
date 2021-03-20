package com.example.mobileprogrammingproject

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    private fun fetchLocation() {
        /*
         * fetchLocation will get the last known location which is also
         * the current location. The function will also update text on the app
         * to display the users current location.
         */
        val task = fusedLocationProviderClient.lastLocation
        val cityName: TextView = findViewById(R.id.textView1)

        /* Self check for permissions. This will prompt the user to allow location access to the phone */
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        /* Listener to get the location and update the text on the app with the current city name */
        task.addOnSuccessListener {
            if (it != null) {
                val geo = Geocoder(this, Locale.getDefault())
                val addresses = geo.getFromLocation(it.latitude,it.longitude,1)
                val locality = addresses[0].locality
                cityName.text = locality
            }
        }
    }

    // Weather API
    object ApiConstant{
        // Can be accessed like so apiConstant.WEATHER_API
        const val WEATHER_API = "a7738a77d04912d0a247200f2a860541"
    }
}