package com.example.climaapp

data class WeatherResponse(
    val main: MainData,
    val weather: List<WeatherData>,
    val name: String,
    val sys: SysData
)

data class MainData(
    val temp: Double,
    val humidity: Int
)

data class WeatherData(
    val description: String
)

data class SysData(
    val country: String
)

data class GeoLocationResponse(
    val name: String,
    val country: String,
    val state: String? = null
)