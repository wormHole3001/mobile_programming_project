@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

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
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var weatherApiUrl: String
    private lateinit var weeklyApiUrl: String
    private var weatherApi = ApiConstant.WEATHER_API
    private lateinit var currentTemp: TextView
    private lateinit var cityName: TextView
    private lateinit var dayDescription: TextView
    private lateinit var sunriseTime: TextView
    private lateinit var sunsetTime: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Link currentTemp; Will be used to display the temperature */
        /* Link cityName; Will be used to display the current city name */
        currentTemp = findViewById(R.id.textView)
        cityName = findViewById(R.id.textView1)
        dayDescription = findViewById(R.id.day_description)
        sunriseTime = findViewById(R.id.textView2)
        sunsetTime = findViewById(R.id.textView3)
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
                weeklyApiUrl = "http://api.weatherbit.io/v2.0/forecast/daily?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + weatherApi
                getTemp()
            }
    }
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getTemp() {
        /*
         * getTemp() will create an API request and parse the returning JSON data.
         * It will then use this information to display to the user different data for
         * their location such as current city and temperature.
         */

        /* Init the RequestQueue */
        val queue = Volley.newRequestQueue(this)
        val url: String = weatherApiUrl
        val weeklyUrl: String = weeklyApiUrl
        
        /* Create request response */
        val currentWeather = StringRequest(Request.Method.GET, url,
            { response ->
                /* Create JSON Object */
                val obj = JSONObject(response)
                /* Get 'data' from JSON object */
                val arr = obj.getJSONArray("data")
                /* Get JSON data from index 0 */
                val obj2 = arr.getJSONObject(0)
                /* Convert celsius to fahrenheit */
                val fahrenheit = obj2.getString("temp")
                val temperature = (fahrenheit.toFloat() * (9.0/5.0) + 32.0).toInt().toString()
                /* Get UTC time for sunrise and sunset */
                val utcTimeSunrise = obj2.getString("sunrise")
                val utcTimeSunset = obj2.getString("sunset")
                val simpleDate = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = simpleDate.format(Date())
                val dateTimeSunrise = "$currentDate $utcTimeSunrise"
                val dateTimeSunset = "$currentDate $utcTimeSunset"
                /* Show the user information by making changes to the UI */
                currentTemp.text = "$temperature°"
                cityName.text = obj2.getString("city_name")
                dayDescription.text = obj.getJSONArray("data").getJSONObject(0).getJSONObject("weather").getString("description")
                sunriseTime.text = dateTimeSunrise.toDate().formatTo("hh:mm a")
                sunsetTime.text = dateTimeSunset.toDate().formatTo("hh:mm a")
            }
        )
        /* In case of error */
        { println("Something might have gone wrong....") }

        /* Weekly Forecast */
        val weeklyForecast = StringRequest(Request.Method.GET, weeklyUrl,
                { response ->
                    /* TODO: Parse JSON weekly forecast */
                    val obj = JSONObject(response)
                    /* TODO: Update UI */
                }
        )
        /* In case of error */
        { println("Something might have gone wrong.....") }

        queue.add(currentWeather)
        queue.add(weeklyForecast)
    }

    private fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
        /*
         * Function is used to format date for UTC to the local timezone
         */
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    private fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        /*
         * Function is used to format a timestamp to only show HH:mm this is used to showcase the
         * sunrise and sunset
         */
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }


    /* Weather API Object */
    object ApiConstant{
        /* Can be accessed like so ApiConstant.WEATHER_API */
        const val WEATHER_API = "86d9d9dde5944416b9adeb980e522099"
    }
}