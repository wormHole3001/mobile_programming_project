package com.example.mobileprogrammingproject

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private var weatherApiUrl = ""
    private var weatherApi = ApiConstant.WEATHER_API
    private lateinit var currentTemp: TextView
    private lateinit var cityName: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Link currentTemp; Will be used to display the temperature */
        /* Link cityName; Will be used to display the current city name */
        currentTemp = findViewById(R.id.textView)
        cityName = findViewById(R.id.textView1)
        /* Create an instance of the fused location provider */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        obtainLocation()
    }

    private fun obtainLocation() {
        /*
         * obtainLocation() will create a string which is the api call. It will then call the
         * getTemp() function to get the weather for the current device location
         */

        /* Self check for permissions. This will prompt the user to allow location access to the phone */
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                /* Create and api call string using longitude, latitude, and our provided api key */
                weatherApiUrl = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + weatherApi
                getTemp()
            }
    }
    @SuppressLint("SetTextI18n")
    private fun getTemp() {
        /*
         * getTemp() will create an API request and parse the returning JSON data.
         * It will then use this information to display to the user different data for
         * their location such as current city and temperature.
         */

        /* Init the RequestQueue */
        val queue = Volley.newRequestQueue(this)
        val url: String = weatherApiUrl
        
        /* Create request response */
        val stringReq = StringRequest(Request.Method.GET, url,
            { response ->
                /* Create JSON Object */
                val obj = JSONObject(response)
                /* Get 'data' from JSON object */
                val arr = obj.getJSONArray("data")
                /* Get JSON data from index 0 */
                val obj2 = arr.getJSONObject(0)
                /* Show the user useful information by making changes to the UI */
                currentTemp.text = obj2.getString("temp")
                cityName.text = obj2.getString("city_name")
                /*
                 * TODO: Convert celsius to fahrenheit for easier readability in the US.
                 * TODO: Add more useful information related to weather to the user.
                 */
            },
            // In case of any error
            { currentTemp.text = "Something Went Wrong" })
        queue.add(stringReq)
    }

    /* Weather API Object */
    object ApiConstant{
        /* Can be accessed like so ApiConstant.WEATHER_API */
        const val WEATHER_API = "86d9d9dde5944416b9adeb980e522099"
    }
}