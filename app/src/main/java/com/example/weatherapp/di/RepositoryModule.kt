package com.example.weatherapp.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.weatherapp.data.network.api.ApiFactory
import com.example.weatherapp.data.network.api.ApiService
import com.example.weatherapp.data.repository.FavouriteRepositoryImpl
import com.example.weatherapp.data.repository.SearchRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.FavouriteRepository
import com.example.weatherapp.domain.repository.SearchRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindWeatherRepo(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindSearchRepo(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    fun bindFavouriteRepo(impl: FavouriteRepositoryImpl): FavouriteRepository

}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiFactory.apiService
    }

    @Provides
    @Singleton
    fun provideMviStoreFactory(): StoreFactory {
        return DefaultStoreFactory()
    }
}