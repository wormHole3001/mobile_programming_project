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
import java.lang.Exception
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
                weeklyApiUrl = "https://api.weatherbit.io/v2.0/forecast/daily?units=I&" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + weatherApi
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

        /* Weekly Forecast */
        val weeklyForecast = StringRequest(Request.Method.GET, weeklyUrl,
                { response ->
                    /* TODO: Implement the weekly forecast */
                    /* Create JSON Object */
                    val apiObject = JSONObject(response)
                    /* Get 'data' from JSON object */
                    val dataArray = apiObject.getJSONArray("data")
                    /* Parse array create JSON objects for the next 7 days */
                    val dayOneData = dataArray.getJSONObject(0)
                    val dayTwoData = dataArray.getJSONObject(1)
                    val dayThreeData = dataArray.getJSONObject(2)
                    val dayFourData = dataArray.getJSONObject(3)
                    val dayFiveData = dataArray.getJSONObject(4)
                    val daySixData = dataArray.getJSONObject(5)
                    val daySevenData = dataArray.getJSONObject(6)
                    /* Get the temperature for the day */
                    val dayOneFahrenheit = dayOneData.getString("temp")
                    val dayTwoFahrenheit = dayTwoData.getString("temp")
                    val dayThreeFahrenheit = dayThreeData.getString("temp")
                    val dayFourFahrenheit = dayFourData.getString("temp")
                    val dayFiveFahrenheit = dayFiveData.getString("temp")
                    val daySixFahrenheit = daySixData.getString("temp")
                    val daySevenFahrenheit = daySevenData.getString("temp")
                    /* Parse JSON for the sunrise and sunset for the current date */
                    val utcTimeSunrise = dayOneData.getString("sunrise_ts")
                    val utcTimeSunset = dayOneData.getString("sunset_ts")
                    val sunrise = getSunTime(utcTimeSunrise)
                    val sunset = getSunTime(utcTimeSunset)
                    /* Show the user information by making changes to the UI */
                    sunriseTime.text = sunrise
                    sunsetTime.text = sunset
                    currentTemp.text = "$dayOneFahrenheit°"
                    cityName.text = apiObject.getString("city_name")
                    dayDescription.text = apiObject.getJSONArray("data").getJSONObject(0).getJSONObject("weather").getString("description")
                }
        )
        /* In case of error */
        { println("Something might have gone wrong.....") }

        queue.add(weeklyForecast)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getSunTime(s: String): String? {
        /*
         * getSunTime function takes a string which is UTC unix format and returns the
         * time in the following format: HH:mm. It is used to get the sunrise and sunset time
         * for the current date.
         */
        return try {
            val simpleDateFormat = SimpleDateFormat("K:mm a")
            val netDate = Date(s.toLong() * 1000)
            simpleDateFormat.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }


    /* Weather API Object */
    object ApiConstant{
        /* Can be accessed like so ApiConstant.WEATHER_API */
        const val WEATHER_API = "86d9d9dde5944416b9adeb980e522099"
    }
}