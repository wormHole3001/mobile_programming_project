@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.mobileprogrammingproject

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
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
    private lateinit var weeklyApiUrl: String
    private var weatherApi = ApiConstant.WEATHER_API
    private lateinit var currentTemp: TextView
    private lateinit var cityName: TextView
    private lateinit var dayDescription: TextView
    private lateinit var sunriseTime: TextView
    private lateinit var sunsetTime: TextView
    private lateinit var dateTwo: TextView
    private lateinit var dateThree: TextView
    private lateinit var dateFour: TextView
    private lateinit var dateFive: TextView
    private lateinit var dateSix: TextView
    private lateinit var dateSeven: TextView
    private lateinit var dayOneTemp: TextView
    private lateinit var dayTwoTemp: TextView
    private lateinit var dayThreeTemp: TextView
    private lateinit var dayFourTemp: TextView
    private lateinit var dayFiveTemp: TextView
    private lateinit var daySixTemp: TextView
    private lateinit var daySevenTemp: TextView
    private lateinit var dayOneIcon: View
    private lateinit var dayTwoIcon: View
    private lateinit var dayThreeIcon: View
    private lateinit var dayFourIcon: View
    private lateinit var dayFiveIcon: View
    private lateinit var daySixIcon: View
    private lateinit var daySevenIcon: View
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Link currentTemp; Will be used to display the temperature */
        /* Link cityName; Will be used to display the current city name */
        currentTemp = findViewById(R.id.dayTemp)
        cityName = findViewById(R.id.cityName)
        dayDescription = findViewById(R.id.dayDescription)
        sunriseTime = findViewById(R.id.sunriseTime)
        sunsetTime = findViewById(R.id.sunsetTime)
        /* Links to day forecast text view */
        dateTwo = findViewById(R.id.dayTwo)
        dateThree = findViewById(R.id.dayThree)
        dateFour = findViewById(R.id.dayFour)
        dateFive = findViewById(R.id.dayFive)
        dateSix = findViewById(R.id.daySix)
        dateSeven = findViewById(R.id.daySeven)
        /* Links for day icons */
        dayOneIcon = findViewById(R.id.nowIcon)
        dayTwoIcon = findViewById(R.id.dayTwoIcon)
        dayThreeIcon = findViewById(R.id.dayThreeIcon)
        dayFourIcon = findViewById(R.id.dayFourIcon)
        dayFiveIcon = findViewById(R.id.dayFiveIcon)
        daySixIcon = findViewById(R.id.daySixIcon)
        daySevenIcon = findViewById(R.id.daySevenIcon)
        /* Links to temperature for the seven day forecast text views */
        dayOneTemp = findViewById(R.id.nowTemp)
        dayTwoTemp = findViewById(R.id.dayTwoTemp)
        dayThreeTemp = findViewById(R.id.dayThreeTemp)
        dayFourTemp = findViewById(R.id.dayFourTemp)
        dayFiveTemp = findViewById(R.id.dayFiveTemp)
        daySixTemp = findViewById(R.id.daySixTemp)
        daySevenTemp = findViewById(R.id.daySevenTemp)
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
        val weeklyUrl: String = weeklyApiUrl

        /* Weekly Forecast */
        val weeklyForecast = StringRequest(Request.Method.GET, weeklyUrl,
                { response ->
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
                    /* Get the temperature for day 1 to 7 */
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
                    /* Get and set the days for the weekly forecast */
                    val currentDay = SimpleDateFormat("dd")
                    dateTwo.text = (currentDay.format(Date()).toInt() + 1).toString()
                    dateThree.text = (currentDay.format(Date()).toInt() + 2).toString()
                    dateFour.text = (currentDay.format(Date()).toInt() + 3).toString()
                    dateFive.text = (currentDay.format(Date()).toInt() + 4).toString()
                    dateSix.text = (currentDay.format(Date()).toInt() + 5).toString()
                    dateSeven.text = (currentDay.format(Date()).toInt() + 6).toString()
                    /* Set the temperature for the day of the week */
                    dayOneTemp.text = "$dayOneFahrenheit°"
                    dayTwoTemp.text = "$dayTwoFahrenheit°"
                    dayThreeTemp.text = "$dayThreeFahrenheit°"
                    dayFourTemp.text = "$dayFourFahrenheit°"
                    dayFiveTemp.text = "$dayFiveFahrenheit°"
                    daySixTemp.text = "$daySixFahrenheit°"
                    daySevenTemp.text = "$daySevenFahrenheit°"
                    /* Set icons */
                    val dayOneCode = dayOneData.getJSONObject("weather").getString("code")
                    val dayTwoCode = dayTwoData.getJSONObject("weather").getString("code")
                    val dayThreeCode = dayThreeData.getJSONObject("weather").getString("code")
                    val dayFourCode = dayFourData.getJSONObject("weather").getString("code")
                    val dayFiveCode = dayFiveData.getJSONObject("weather").getString("code")
                    val daySixCode = daySixData.getJSONObject("weather").getString("code")
                    val daySevenCode = daySevenData.getJSONObject("weather").getString("code")
                    setDayIcon(dayOneCode,"1")
                    setDayIcon(dayTwoCode,"2")
                    setDayIcon(dayThreeCode,"3")
                    setDayIcon(dayFourCode,"4")
                    setDayIcon(dayFiveCode,"5")
                    setDayIcon(daySixCode,"6")
                    setDayIcon(daySevenCode,"7")
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

    private fun setDayIcon(code: String, day: String) {
        when (day) {
            "1" -> {
                when (code) {
                    "200" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        dayOneIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "2" -> {
                when (code) {
                    "200" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        dayTwoIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "3" -> {
                when (code) {
                    "200" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        dayThreeIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "4" -> {
                when (code) {
                    "200" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        dayFourIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "5" -> {
                when (code) {
                    "200" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        dayFiveIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "6" -> {
                when (code) {
                    "200" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        daySixIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
            "7" -> {
                when (code) {
                    "200" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_200)
                    }
                    "201" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_201)
                    }
                    "202" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_202)
                    }
                    "230" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_230)
                    }
                    "231" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_231)
                    }
                    "232" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_232)
                    }
                    "233" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_233)
                    }
                    "300" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_300)
                    }
                    "301" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_301)
                    }
                    "302" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_302)
                    }
                    "500" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_500)
                    }
                    "501" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_501)
                    }
                    "502" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_502)
                    }
                    "511" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_511)
                    }
                    "520" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_520)
                    }
                    "521" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_521)
                    }
                    "522" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_522)
                    }
                    "600" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_600)
                    }
                    "601" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_601)
                    }
                    "602" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_602)
                    }
                    "610" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_610)
                    }
                    "611" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_611)
                    }
                    "612" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_612)
                    }
                    "621" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_621)
                    }
                    "622" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_622)
                    }
                    "623" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_623)
                    }
                    "700" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_700)
                    }
                    "711" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_711)
                    }
                    "721" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_721)
                    }
                    "731" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_731)
                    }
                    "741" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_741)
                    }
                    "751" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_751)
                    }
                    "800" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_800)
                    }
                    "801" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_801)
                    }
                    "802" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_802)
                    }
                    "803" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_803)
                    }
                    "804" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_804)
                    }
                    "900" -> {
                        daySevenIcon.setBackgroundResource(R.drawable.code_900)
                    }
                }
            }
        }
    }


    /* Weather API Object */
    object ApiConstant{
        /* Can be accessed like so ApiConstant.WEATHER_API */
        const val WEATHER_API = "86d9d9dde5944416b9adeb980e522099"
    }
}