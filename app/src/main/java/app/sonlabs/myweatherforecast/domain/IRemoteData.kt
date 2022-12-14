package app.sonlabs.myweatherforecast.domain

import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IRemoteData {

    @Headers("Content-Type: application/json")
    @GET("weather")
    suspend fun searchCityFromApi(
        @Query("q") name: String,
        @Query("units") units: String,
    ): Forecast

    @Headers("Content-Type: application/json")
    @GET("weather")
    suspend fun searchCityByLocationFromApi(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
    ): Forecast

    @Headers("Content-Type: application/json")
    @GET("onecall")
    suspend fun getDailyFromApi(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
    ): ForecastDetail
}
