package app.sonlabs.myweatherforecast.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UseCaseInOut<in IN, out OUT> {
    suspend fun execute(param: IN): Flow<OUT>
}
