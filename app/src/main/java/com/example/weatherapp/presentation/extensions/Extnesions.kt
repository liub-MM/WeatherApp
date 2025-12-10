package com.example.weatherapp.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.example.weatherapp.presentation.favourite.FavouriteComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


fun ComponentContext.scope ()  = CoroutineScope(
    Dispatchers.Main + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}