package com.example.mobileprogrammingproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Weather API
    object ApiConstant{
        // Can be accessed like so apiConstant.WEATHER_API
        const val WEATHER_API = "a7738a77d04912d0a247200f2a860541"
    }
}