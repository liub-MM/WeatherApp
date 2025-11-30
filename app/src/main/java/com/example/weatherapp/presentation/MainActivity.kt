package com.example.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.weatherapp.data.network.api.ApiFactory
import com.example.weatherapp.data.network.api.ApiService
import com.example.weatherapp.presentation.theme.ui.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiFactory.apiService
        CoroutineScope(Dispatchers.Main).launch{
            val weather = apiService.loadCurrentWeather("London")
            val fore = apiService.loadForecastWeather("London")
            val s = apiService.searchCity("London")

            Log.d("MainActivity", "$weather")
            Log.d("MainActivity", "$fore")
            Log.d("MainActivity", "$s")

        }
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {


            }
        }
    }
}