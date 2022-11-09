package app.sonlabs.myweatherforecast.domain.usecase

import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.domain.IRepository
import app.sonlabs.myweatherforecast.domain.SearchCityParams
import kotlinx.coroutines.flow.Flow

class SearchCityUseCase(
    private val repository: IRepository
) : UseCaseInOut<SearchCityParams, Forecast> {
    override suspend fun execute(param: SearchCityParams): Flow<Forecast> =
        repository.searchCity(param)
}
