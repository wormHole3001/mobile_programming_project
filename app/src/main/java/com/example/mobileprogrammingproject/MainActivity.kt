package com.example.mobileprogrammingproject

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


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
                val tv1: TextView = findViewById(R.id.textView1)
                tv1.text = "${it.latitude} -- ${it.longitude}"
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Weather API
    object ApiConstant{
        // Can be accessed like so apiConstant.WEATHER_API
        const val WEATHER_API = "a7738a77d04912d0a247200f2a860541"
    }
}