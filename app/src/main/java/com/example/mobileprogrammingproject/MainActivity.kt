package com.example.mobileprogrammingproject

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        val tv1: TextView = findViewById(R.id.textView1)
        task.addOnSuccessListener {
            if (it != null) {

                // TEST CITY
                val geo = Geocoder(this, Locale.getDefault())
                val addresses = geo.getFromLocation(it.latitude,it.longitude,1)
                val locality = addresses[0].locality

                val cityName: TextView = findViewById(R.id.textView1)
                cityName.text = locality
                //Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Weather API
    object ApiConstant{
        // Can be accessed like so apiConstant.WEATHER_API
        const val WEATHER_API = "a7738a77d04912d0a247200f2a860541"
    }
}