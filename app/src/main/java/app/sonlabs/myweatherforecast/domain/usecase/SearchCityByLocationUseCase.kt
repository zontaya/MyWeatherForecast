package app.sonlabs.myweatherforecast.domain.usecase

import app.sonlabs.myweatherforecast.data.Forecast
import app.sonlabs.myweatherforecast.domain.IRepository
import app.sonlabs.myweatherforecast.domain.SearchCityByLocationParams
import kotlinx.coroutines.flow.Flow

class SearchCityByLocationUseCase(
    private val repository: IRepository
) : UseCaseInOut<SearchCityByLocationParams, Forecast> {
    override suspend fun execute(param: SearchCityByLocationParams): Flow<Forecast> =
        repository.searchCityByLocation(param)
}
