package com.example.weatherapp.data.network.dto

import android.icu.util.Calendar
import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("day") val dayWeatherDto: DayWeatherDto,
    @SerializedName("date_epoch") val date : Long
)
