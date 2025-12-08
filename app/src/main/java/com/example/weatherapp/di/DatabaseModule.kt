package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.local.db.FavouriteCitiesDao
import com.example.weatherapp.data.local.db.FavouriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : FavouriteDatabase {
        return FavouriteDatabase.getInstance(context)
    }

    @Provides
    fun provideFavouriteCitiesDao (db: FavouriteDatabase) : FavouriteCitiesDao {
        return db.favouriteCitiesDao()
    }
}