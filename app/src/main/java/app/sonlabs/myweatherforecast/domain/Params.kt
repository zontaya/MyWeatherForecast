package app.sonlabs.myweatherforecast.domain

import app.sonlabs.myweatherforecast.data.Coord

class SearchCityParams(
    val name: String,
    val units: String
)

class SearchCityByLocationParams(
    val location: Coord,
    val units: String
)

class GetDailyParams(
    val lat: Double,
    val lon: Double,
    val units: String,
    val exclude: String = "daily"
)
