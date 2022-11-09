package app.sonlabs.myweatherforecast.domain.usecase

import app.sonlabs.myweatherforecast.data.remote.ForecastDetail
import app.sonlabs.myweatherforecast.domain.GetDailyParams
import app.sonlabs.myweatherforecast.domain.IRepository
import kotlinx.coroutines.flow.Flow

class GetDailyUseCase(
    private val repository: IRepository
) : UseCaseInOut<GetDailyParams, ForecastDetail> {
    override suspend fun execute(param: GetDailyParams): Flow<ForecastDetail> =
        repository.getDaily(param)
}
