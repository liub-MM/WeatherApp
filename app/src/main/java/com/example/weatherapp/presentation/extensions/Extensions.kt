package com.example.weatherapp.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


fun ComponentContext.scope ()  = CoroutineScope(
    Dispatchers.Main + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

fun Float.tempToFormattedString(): String = "${roundToInt()}°C"

fun Calendar.formattedFullDate(): String {
    val format = SimpleDateFormat("EEEE | d MMM y", Locale.getDefault())
    return format.format(time)
}

fun Calendar.formattedShortDayOfWeek(): String {
    val format = SimpleDateFormat("EEE", Locale.getDefault())
    return format.format(time)
}
