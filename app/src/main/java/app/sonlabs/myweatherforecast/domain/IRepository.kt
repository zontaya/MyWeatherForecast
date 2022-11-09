package app.sonlabs.myweatherforecast.domain

import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun searchCity(param: SearchCityParams): Flow<Forecast>
    suspend fun searchCityByLocation(param: SearchCityByLocationParams): Flow<Forecast>
    suspend fun getDaily(param: GetDailyParams): Flow<ForecastDetail>
}
