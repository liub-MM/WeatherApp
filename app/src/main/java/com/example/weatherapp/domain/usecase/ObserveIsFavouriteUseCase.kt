package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavouriteUseCase @Inject constructor(private val repository: FavouriteRepository) {

    operator fun invoke(cityID: Int) = repository.observeIsFavourite(cityID)

}