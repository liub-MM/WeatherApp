package com.example.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("condition") val conditionDto: ConditionDto,
    @SerializedName("avgtemp_c") val temp : Float,
)
