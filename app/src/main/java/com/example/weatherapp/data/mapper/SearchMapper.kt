package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.network.dto.CityDto
import com.example.weatherapp.domain.entities.City

fun CityDto.toEntity() : City = City(id,name,country)